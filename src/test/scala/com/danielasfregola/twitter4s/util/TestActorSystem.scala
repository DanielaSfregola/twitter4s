package com.danielasfregola.twitter4s.util

import akka.actor.ActorSystem
import akka.testkit.TestKit

import com.danielasfregola.twitter4s.providers.ActorRefFactoryProvider

abstract class TestActorSystem extends TestKit(ActorSystem()) with ActorRefFactoryProvider {
  implicit val actorRefFactory = system
}
