package com.danielasfregola.twitter4s.processors

import com.danielasfregola.twitter4s.entities.streaming.{Event, StreamingUpdate}
import com.danielasfregola.twitter4s.http.unmarshalling.JsonSupport
import com.typesafe.scalalogging.LazyLogging
import org.json4s.native.Serialization

object TwitterProcessor extends LazyLogging with JsonSupport {

  def echo(update: StreamingUpdate): Unit =
    update match {
      case StreamingUpdate(event: Event) => println(Serialization.write(event))//logger.info("{}", Serialization.write(event))
      case StreamingUpdate(msg) => println(Serialization.write(msg))//logger.warn("{}", Serialization.write(msg))
    }

  def dot(update: StreamingUpdate): Unit = print(".")
}
