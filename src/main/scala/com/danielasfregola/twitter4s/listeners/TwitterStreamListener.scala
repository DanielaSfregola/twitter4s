package com.danielasfregola.twitter4s.listeners

import akka.actor.Actor
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate

abstract class TwitterStreamListener extends Actor {

  def receive: Receive = {
    case update: StreamingUpdate => processStreamingUpdate(update)
  }

  def processStreamingUpdate(streamingUpdate: StreamingUpdate): Unit
}
