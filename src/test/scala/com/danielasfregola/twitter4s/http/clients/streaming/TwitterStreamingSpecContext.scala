package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.streaming.{StreamingMessage, StreamingUpdate}
import com.danielasfregola.twitter4s.util.OldClientSpecContext

class TwitterStreamingSpecContext extends OldClientSpecContext with ImplicitSender {

  private[twitter4s] def createListener(f: PartialFunction[StreamingMessage, Unit]): ActorRef = self

  def dummyProcessing: PartialFunction[StreamingMessage, Unit] = { case _ => }
}
