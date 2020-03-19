// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.actors.supervision;

import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Supervised;
import io.vlingo.actors.SupervisionStrategy;
import io.vlingo.actors.Supervisor;

public class EscalateSupervisorActor extends Actor implements Supervisor {
  private final EscalateSupervisorTestResults testResults;
  
  public EscalateSupervisorActor(final EscalateSupervisorTestResults testResults) {
    this.testResults = testResults;
  }
  
  private final SupervisionStrategy strategy =
          new SupervisionStrategy() {
            @Override
            public int intensity() {
              return 5;
            }

            @Override
            public long period() {
              return 1000;
            }

            @Override
            public Scope scope() {
              return Scope.One;
            }
          };
  
  @Override
  public void inform(final Throwable throwable, final Supervised supervised) {
    testResults.informedCount.incrementAndGet();
    
    supervised.escalate();
  }

  @Override
  public SupervisionStrategy supervisionStrategy() {
    return strategy;
  }

  public static class EscalateSupervisorTestResults {
    public AtomicInteger informedCount = new AtomicInteger(0);
  }
}
