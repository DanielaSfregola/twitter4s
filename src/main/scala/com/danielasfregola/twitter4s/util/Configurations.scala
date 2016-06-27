package com.danielasfregola.twitter4s.util

import com.typesafe.config.ConfigFactory

import scala.util.Properties

trait Configurations {

  protected def config = ConfigFactory.load

  protected val twitterVersion = "1.1"

  protected val apiTwitterUrl = "https://api.twitter.com"
  protected val mediaTwitterUrl = "https://upload.twitter.com"

}

object TokensFromConfig extends Configurations {

  val consumerTokenKey = Properties.envOrElse(
    "TWITTER_CONSUMER_TOKEN_KEY", config.getString("twitter.consumer.key"))
  val consumerTokenSecret = Properties.envOrElse(
    "TWITTER_CONSUMER_TOKEN_SECRET", config.getString("twitter.consumer.secret"))

  val accessTokenKey = Properties.envOrElse(
    "TWITTER_ACCESS_TOKEN_KEY", config.getString("twitter.access.key"))
  val accessTokenSecret = Properties.envOrElse(
    "TWITTER_ACCESS_TOKEN_SECRET", config.getString("twitter.access.secret"))
}
