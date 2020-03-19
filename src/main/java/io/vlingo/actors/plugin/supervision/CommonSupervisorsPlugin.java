// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.actors.plugin.supervision;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Configuration;
import io.vlingo.actors.Registrar;
import io.vlingo.actors.plugin.AbstractPlugin;
import io.vlingo.actors.plugin.Plugin;
import io.vlingo.actors.plugin.PluginConfiguration;
import io.vlingo.actors.plugin.PluginProperties;

public class CommonSupervisorsPlugin extends AbstractPlugin implements Plugin {
  private final CommonSupervisorsPluginConfiguration configuration;

  public CommonSupervisorsPlugin() {
    this.configuration = new CommonSupervisorsPluginConfiguration();
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
    return "common_supervisors";
  }

  @Override
  public int pass() {
    return 2;
  }

  @Override
  public void start(final Registrar registrar) {
    for (final ConfiguredSupervisor supervisor : configuration.supervisors) {
      registrar.registerCommonSupervisor(supervisor.stageName, supervisor.supervisorName, supervisor.supervisedProtocol, supervisor.supervisorClass);
    }
  }

  public static class CommonSupervisorsPluginConfiguration implements PluginConfiguration {
    private final List<ConfiguredSupervisor> supervisors;

    public static CommonSupervisorsPluginConfiguration define() {
      return new CommonSupervisorsPluginConfiguration();
    }

    public CommonSupervisorsPluginConfiguration supervisor(final String stageName, final String supervisorName, final Class<?> supervisedProtocol, final Class<? extends Actor> supervisorClass) {
      supervisors.add(new ConfiguredSupervisor(stageName, supervisorName, supervisedProtocol, supervisorClass));
      return this;
    }

    public int count() {
      return supervisors.size();
    }

    public String name(final int index) {
      return supervisors.get(index).supervisorName;
    }

    public String stageName(final int index) {
      return supervisors.get(index).stageName;
    }

    public Class<?> supervisedProtocol(final int index) {
      return supervisors.get(index).supervisedProtocol;
    }

    public Class<? extends Actor> supervisorClass(final int index) {
      return supervisors.get(index).supervisorClass;
    }

    @Override
    public void build(final Configuration configuration) {
    }

    @Override
    public void buildWith(final Configuration configuration, final PluginProperties properties) {
      for (final DefinitionValues values : DefinitionValues.allDefinitionValues(properties)) {
        final ConfiguredSupervisor supervisor =
                new ConfiguredSupervisor(
                        values.stageName,
                        values.name,
                        values.protocol,
                        values.supervisor);

        if (supervisors.contains(supervisor)) {
          supervisors.remove(supervisor);
        } 
        supervisors.add(supervisor);
      }
      configuration.with(this);
    }

    @Override
    public String name() {
      return name(0);
    }

    private CommonSupervisorsPluginConfiguration() {
      this.supervisors = new ArrayList<>();
    }
  }
}
