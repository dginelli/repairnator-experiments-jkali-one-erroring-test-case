/*
 * Copyright (c) 2017, Cameron <moberg@tuta.io>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.xptracker;

import static com.google.common.base.MoreObjects.firstNonNull;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Binder;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.events.ExperienceChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import static net.runelite.client.plugins.xptracker.XpWorldType.NORMAL;

import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginToolbar;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldClient;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;
import net.runelite.http.api.xp.XpClient;

@PluginDescriptor(
	name = "XP Tracker"
)
@Slf4j
public class XpTrackerPlugin extends Plugin
{
	@Inject
	private PluginToolbar pluginToolbar;

	@Inject
	private Client client;

	@Inject
	private SkillIconManager skillIconManager;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private XpTrackerConfig xpTrackerConfig;

	private NavigationButton navButton;
	private XpPanel xpPanel;

	private final Map<Skill, SkillXPInfo> xpInfos = new HashMap<>();

	private WorldResult worlds;
	private XpWorldType lastWorldType;
	private String lastUsername;

	private final XpClient xpClient = new XpClient();

	@Override
	public void configure(Binder binder)
	{
		binder.bind(XpTrackerService.class).to(XpTrackerServiceImpl.class);
	}

	@Provides
	XpTrackerConfig getConfig(ConfigManager configManager) {
		return configManager.getConfig(XpTrackerConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		WorldClient worldClient = new WorldClient();
		try
		{
			worlds = worldClient.lookupWorlds();
			if (worlds != null)
			{
				log.debug("Worlds list contains {} worlds", worlds.getWorlds().size());
			}
			else
			{
				log.warn("Unable to look up worlds");
			}
		}
		catch (IOException e)
		{
			log.warn("Error looking up worlds list", e);
		}

		xpPanel = new XpPanel(this, client, skillIconManager);

		BufferedImage icon;
		synchronized (ImageIO.class)
		{
			icon = ImageIO.read(getClass().getResourceAsStream("xp.png"));
		}

		navButton = NavigationButton.builder()
			.name("XP Tracker")
			.icon(icon)
			.panel(xpPanel)
			.build();

		pluginToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		pluginToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			// LOGGED_IN is triggered between region changes too.
			// Check that the username changed or the world type changed.
			XpWorldType type = getWorldType(client.getWorld());

			if (!Objects.equals(client.getUsername(), lastUsername) || lastWorldType != type)
			{
				// Reset
				log.debug("World change: {} -> {}, {} -> {}",
					lastUsername, client.getUsername(),
					firstNonNull(lastWorldType, "<unknown>"),
					firstNonNull(type, "<unknown>"));

				lastUsername = client.getUsername();
				lastWorldType = type;
				xpPanel.resetAllInfoBoxes();
			}
		}
		else if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			Player local = client.getLocalPlayer();
			String username = local != null ? local.getName() : null;
			if (username != null)
			{
				log.debug("Submitting xp track for {}", username);

				executor.submit(() ->
				{
					try
					{
						xpClient.update(username);
					}
					catch (IOException ex)
					{
						log.warn("error submitting xp track", ex);
					}
				});
			}
		}
	}

	private XpWorldType getWorldType(int worldNum)
	{
		if (worlds == null)
		{
			return null;
		}

		World world = worlds.findWorld(worldNum);

		if (world == null)
		{
			log.warn("Logged into nonexistent world {}?", client.getWorld());
			return null;
		}

		XpWorldType type = worldSetToType(world.getTypes());
		return type;
	}

	private XpWorldType worldSetToType(EnumSet<WorldType> types)
	{
		XpWorldType xpType = NORMAL;
		for (WorldType type : types)
		{
			XpWorldType t = XpWorldType.of(type);
			if (t != NORMAL)
			{
				xpType = t;
			}
		}
		return xpType;
	}

	public SkillXPInfo getSkillXpInfo(Skill skill)
	{
		return xpInfos.computeIfAbsent(skill, (s) -> new SkillXPInfo(s, xpTrackerConfig));
	}

	@Subscribe
	public void onXpChanged(ExperienceChanged event)
	{
		xpPanel.updateSkillExperience(event.getSkill());
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		xpPanel.updateAllInfoBoxes();
	}
}