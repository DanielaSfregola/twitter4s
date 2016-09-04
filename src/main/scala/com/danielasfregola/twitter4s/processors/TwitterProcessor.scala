package com.danielasfregola.twitter4s.processors

import com.danielasfregola.twitter4s.entities.streaming.{Event, StreamingUpdate}
import com.typesafe.scalalogging.LazyLogging

object TwitterProcessor extends LazyLogging {

  def echo(update: StreamingUpdate): Unit =
    update match {
      case StreamingUpdate(event: Event) => logger.info("{}", event)
      case StreamingUpdate(msg) => logger.warn("{}", msg)
    }

  def dot(update: StreamingUpdate): Unit = print(".")
}
