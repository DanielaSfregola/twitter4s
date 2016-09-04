package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate
import com.danielasfregola.twitter4s.http.clients.TwitterStreamListener
import com.danielasfregola.twitter4s.util.ClientSpecContext

import scala.reflect.ClassTag

trait TwitterStreamingSpecContext extends ClientSpecContext with ImplicitSender {

  private[twitter4s] def createListener(f: StreamingUpdate => Unit): ActorRef = self

  def dummyProcessing(update: StreamingUpdate): Unit = ()
}
