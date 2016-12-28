package com.danielasfregola.twitter4s.providers

import akka.actor.{ActorRefFactory, ActorSystem}
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

private[twitter4s] trait ActorSystemProvider {

  implicit def system: ActorSystem
  implicit val materializer = ActorMaterializer()
}
