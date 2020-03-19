// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.actors.plugin.mailbox;

import io.vlingo.actors.MailboxProviderKeeper;
import io.vlingo.actors.Registrar;
import io.vlingo.actors.plugin.Plugin;
import io.vlingo.actors.plugin.PluginConfiguration;

public class DefaultMailboxProviderKeeperPlugin implements Plugin {
  private final MailboxProviderKeeper keeper;
  private final DefaultMailboxProviderKeeperPluginConfiguration configuration;

  public DefaultMailboxProviderKeeperPlugin(final MailboxProviderKeeper keeper, final DefaultMailboxProviderKeeperPluginConfiguration configuration) {
    this.keeper = keeper;
    this.configuration = configuration;
  }

  public DefaultMailboxProviderKeeperPlugin() {
    this(new DefaultMailboxProviderKeeper(), new DefaultMailboxProviderKeeperPluginConfiguration());
  }

  @Override
  public void close() {
  }

  @Override
  public PluginConfiguration configuration() {
    return configuration;
  }

  @Override
  public String name() {
    return configuration.name();
  }

  @Override
  public int pass() {
    return 0;
  }

  @Override
  public void start(final Registrar registrar) {
    registrar.registerMailboxProviderKeeper(keeper);
  }
}
