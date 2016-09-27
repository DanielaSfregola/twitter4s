package com.danielasfregola.twitter4s.processors

import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.http.unmarshalling.JsonSupport
import com.typesafe.scalalogging.LazyLogging
import org.json4s.native.Serialization

object TwitterProcessor extends LazyLogging with JsonSupport {

  def echo(msg: StreamingMessage): Unit =
    msg match {
      case tweet: Tweet => logger.info("{}", Serialization.write(tweet))
      case _ => logger.warn("{}", Serialization.write(msg))
    }

  def logTweetText(msg: StreamingMessage): Unit =
    msg match {
      case tweet: Tweet => logger.info(tweet.text)
      case _ =>
    }
}
