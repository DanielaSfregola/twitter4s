package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.streaming.{StreamingMessage, StreamingUpdate}
import com.danielasfregola.twitter4s.util.ClientSpecContext

class TwitterStreamingSpecContext extends ClientSpecContext with ImplicitSender {

  private[twitter4s] def createListener(f: StreamingMessage => Unit): ActorRef = self

  def dummyProcessing(update: StreamingMessage): Unit = ()
}
