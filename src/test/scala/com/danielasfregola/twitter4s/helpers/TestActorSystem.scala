package com.danielasfregola.twitter4s.helpers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.testkit.TestKit

abstract class TestActorSystem extends TestKit(ActorSystem()) {

  implicit val materializer = ActorMaterializer()
}
