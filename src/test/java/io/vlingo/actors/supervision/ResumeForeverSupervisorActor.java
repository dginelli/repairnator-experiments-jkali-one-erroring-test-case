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
import io.vlingo.actors.testkit.TestUntil;

public class ResumeForeverSupervisorActor extends Actor implements Supervisor {
  private final ResumeForeverSupervisorTestResults testResults;
  
  public ResumeForeverSupervisorActor(final ResumeForeverSupervisorTestResults testResults) {
    this.testResults = testResults;
  }
  
  private final SupervisionStrategy strategy =
          new SupervisionStrategy() {
            @Override
            public int intensity() {
              return SupervisionStrategy.ForeverIntensity;
            }

            @Override
            public long period() {
              return SupervisionStrategy.ForeverPeriod;
            }

            @Override
            public Scope scope() {
              return Scope.One;
            }
          };
  
  @Override
  public void inform(final Throwable throwable, final Supervised supervised) {
    testResults.informedCount.incrementAndGet();
    if (testResults.informedCount.get() == 1) {
      supervised.restartWithin(strategy.period(), strategy.intensity(), strategy.scope());
    } else {
      supervised.resume();
    }
    testResults.untilInform.happened();
  }

  @Override
  public SupervisionStrategy supervisionStrategy() {
    return strategy;
  }

  public static class ResumeForeverSupervisorTestResults {
    public final AtomicInteger informedCount = new AtomicInteger(0);
    public TestUntil untilInform = TestUntil.happenings(0);
  }
}
