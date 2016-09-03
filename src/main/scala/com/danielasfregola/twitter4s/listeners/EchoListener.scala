package com.danielasfregola.twitter4s.listeners

import akka.actor.ActorLogging
import com.danielasfregola.twitter4s.entities.streaming.{Event, StreamingUpdate}

class EchoListener extends TwitterStreamListener with ActorLogging {

  def processStreamingUpdate(streamingUpdate: StreamingUpdate): Unit =
    streamingUpdate match {
      case StreamingUpdate(event: Event) => log.info(event.toString)
      case StreamingUpdate(msg) => log.warning(msg.toString)
    }
}
