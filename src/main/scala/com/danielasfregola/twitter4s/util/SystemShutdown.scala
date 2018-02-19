package com.danielasfregola.twitter4s.util

import akka.actor.ActorSystem

import scala.concurrent.Future

trait SystemShutdown {

  protected def system: ActorSystem

  /** Shutdown of the actor system associated to the client
    */
  def shutdown(): Future[Unit] = {
    implicit val ec = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }
}
