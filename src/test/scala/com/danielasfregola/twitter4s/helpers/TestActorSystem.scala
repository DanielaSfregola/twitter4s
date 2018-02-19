package com.danielasfregola.twitter4s.helpers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

abstract class TestActorSystem {

  implicit val system = TestSystem.system

  implicit val materializer = ActorMaterializer()
}

object TestSystem {

  lazy val system = ActorSystem("test-system")
}
