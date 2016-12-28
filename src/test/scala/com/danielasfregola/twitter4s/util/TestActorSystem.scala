package com.danielasfregola.twitter4s.util

import akka.actor.ActorSystem
import akka.testkit.TestKit

import com.danielasfregola.twitter4s.providers.ActorSystemProvider

abstract class TestActorSystem extends TestKit(TestActorSystem.system) with ActorSystemProvider

object TestActorSystem {
  lazy val system = ActorSystem("twitter4s-unit-tests")
}
