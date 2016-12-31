package com.danielasfregola.twitter4s.providers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

private[twitter4s] trait ActorSystemProvider {

  implicit def system: ActorSystem
  implicit val materializer = ActorMaterializer()
}
