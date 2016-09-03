package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate
import com.danielasfregola.twitter4s.listeners.TwitterStreamListener
import com.danielasfregola.twitter4s.util.ClientSpecContext

import scala.reflect.ClassTag

trait TwitterStreamingSpecContext extends ClientSpecContext with ImplicitSender {

  private[twitter4s] def attachListener[Listener <: TwitterStreamListener : ClassTag]: ActorRef = self

  class DummyListener extends TwitterStreamListener {
    def processStreamingUpdate(streamingUpdate: StreamingUpdate): Unit = ()
  }
}
