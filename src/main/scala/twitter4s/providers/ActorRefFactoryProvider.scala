package twitter4s.providers

import akka.actor.ActorRefFactory

trait ActorRefFactoryProvider {

  implicit def actorRefFactory: ActorRefFactory
}
