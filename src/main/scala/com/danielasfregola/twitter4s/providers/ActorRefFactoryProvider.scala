package com.danielasfregola.twitter4s.providers

import akka.actor.ActorRefFactory

private[twitter4s] trait ActorRefFactoryProvider {

  implicit def actorRefFactory: ActorRefFactory
}
