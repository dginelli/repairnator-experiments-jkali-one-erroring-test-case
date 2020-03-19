/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package net.runelite.mixins;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.runelite.api.Varbits;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSNodeCache;
import net.runelite.rs.api.RSVarbit;

@Mixin(RSClient.class)
public abstract class VarbitMixin implements RSClient
{
	@Shadow("clientInstance")
	private static RSClient client;

	@Inject
	private Cache<Integer, RSVarbit> varbitCache = CacheBuilder.newBuilder()
		.maximumSize(128)
		.build();

	@Inject
	VarbitMixin()
	{
	}

	@Inject
	@Override
	public int getSetting(Varbits varbit)
	{
		int varbitId = varbit.getId();
		return getVarbitValue(varbitId);
	}

	@Inject
	@Override
	public void setSetting(Varbits varbit, int value)
	{
		int varbitId = varbit.getId();
		setVarbitValue(varbitId, value);
	}

	@Inject
	@Override
	public int getVarbitValue(int varbitId)
	{
		RSVarbit v = varbitCache.getIfPresent(varbitId);
		if (v == null)
		{
			client.getVarbit(varbitId); // load varbit into cache
			RSNodeCache varbits = client.getVarbitCache();
			v = (RSVarbit) varbits.get(varbitId); // get from cache
			varbitCache.put(varbitId, v);
		}

		int[] varps = getVarps();
		int value = varps[v.getIndex()];
		int lsb = v.getLeastSignificantBit();
		int msb = v.getMostSignificantBit();
		int mask = (1 << ((msb - lsb) + 1)) - 1;
		return (value >> lsb) & mask;
	}

	@Inject
	@Override
	public void setVarbitValue(int varbitId, int value)
	{
		RSVarbit v = varbitCache.getIfPresent(varbitId);
		if (v == null)
		{
			client.getVarbit(varbitId); // load varbit into cache
			RSNodeCache varbits = client.getVarbitCache();
			v = (RSVarbit) varbits.get(varbitId); // get from cache
			varbitCache.put(varbitId, v);
		}

		int[] varps = getVarps();
		int lsb = v.getLeastSignificantBit();
		int msb = v.getMostSignificantBit();
		int mask = (1 << ((msb - lsb) + 1)) - 1;
		varps[v.getIndex()] = (varps[v.getIndex()] & ~(mask << lsb)) | ((value & mask) << lsb);
	}
}