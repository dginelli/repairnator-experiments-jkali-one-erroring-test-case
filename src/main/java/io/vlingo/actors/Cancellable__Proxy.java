// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.actors;

import java.util.function.Consumer;

public class Cancellable__Proxy implements Cancellable {
  private final Actor actor;
  private final Mailbox mailbox;

  public Cancellable__Proxy(final Actor actor, final Mailbox mailbox) {
    this.actor = actor;
    this.mailbox = mailbox;
  }

  @Override
  public boolean cancel() {
    if (!actor.isStopped()) {
      final Consumer<Cancellable> consumer = (actor) -> actor.cancel();
      mailbox.send(new LocalMessage<Cancellable>(actor, Cancellable.class, consumer, "cancel()"));
      return true;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, "cancel()"));
      return false;
    }
  }
}
