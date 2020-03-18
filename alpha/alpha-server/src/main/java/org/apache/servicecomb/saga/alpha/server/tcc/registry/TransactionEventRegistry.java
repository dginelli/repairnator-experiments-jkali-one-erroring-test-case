/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.servicecomb.saga.alpha.server.tcc.registry;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.servicecomb.saga.alpha.server.tcc.event.ParticipatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage TCC transaction event.
 */
public final class TransactionEventRegistry {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final static Map<String, Set<ParticipatedEvent>> REGISTRY = new ConcurrentHashMap<>();

  /**
   * Register participate event.
   *
   * @param participateEvent participate event
   */
  public static void register(ParticipatedEvent participateEvent) {
    REGISTRY
        .computeIfAbsent(participateEvent.getGlobalTxId(), key -> new LinkedHashSet<>())
        .add(participateEvent);

    LOG.info("Registered participated event, global tx: {}, local tx: {}, parent id: {}, "
            + "confirm: {}, cancel: {}, status: {}, service [{}] instanceId [{}]",
        participateEvent.getGlobalTxId(), participateEvent.getLocalTxId(), participateEvent.getParentTxId(),
        participateEvent.getConfirmMethod(), participateEvent.getCancelMethod(), participateEvent.getStatus(),
        participateEvent.getServiceName(), participateEvent.getInstanceId());
  }

  /**
   * Retrieve participate event from registry.
   *
   * @param globalTxId global transaction id
   * @return participate events
   */
  public static Set<ParticipatedEvent> retrieve(String globalTxId) {
    return REGISTRY.get(globalTxId);
  }
}
