/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.druid.metadata.MetadataRuleManagerConfig;
import io.druid.metadata.MetadataSegmentManagerConfig;
import io.druid.metadata.MetadataStorageConnectorConfig;
import io.druid.metadata.MetadataStorageTablesConfig;

public class MetadataConfigModule implements Module
{
  @Override
  public void configure(Binder binder)
  {
    JsonConfigProvider.bind(binder, "druid.metadata.storage.tables", MetadataStorageTablesConfig.class);
    JsonConfigProvider.bind(binder, "druid.metadata.storage.connector", MetadataStorageConnectorConfig.class);

    JsonConfigProvider.bind(binder, "druid.manager.segments", MetadataSegmentManagerConfig.class);
    JsonConfigProvider.bind(binder, "druid.manager.rules", MetadataRuleManagerConfig.class);
  }
}
