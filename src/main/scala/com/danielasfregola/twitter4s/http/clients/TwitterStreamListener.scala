package com.danielasfregola.twitter4s.http.clients

import akka.actor.Actor
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate

private[twitter4s] abstract class TwitterStreamListener extends Actor {

  def receive: Receive = {
    case update: StreamingUpdate if processStreamingUpdate.isDefinedAt(update) =>
      processStreamingUpdate(update)
    case _ => ()
  }

  def processStreamingUpdate: PartialFunction[StreamingUpdate, Unit]
}
