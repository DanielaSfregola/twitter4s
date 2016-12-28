package com.danielasfregola.twitter4s.processors

import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.http.unmarshalling.OldJsonSupport
import com.typesafe.scalalogging.LazyLogging
import org.json4s.native.Serialization

object TwitterProcessor extends LazyLogging with OldJsonSupport {

  def echo: PartialFunction[StreamingMessage, Unit] = {
    case msg => logger.info("{}", Serialization.write(msg))
  }

  def logTweetText: PartialFunction[StreamingMessage, Unit] = {
    case tweet: Tweet => logger.info(tweet.text)
  }
}
