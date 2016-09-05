package com.danielasfregola.twitter4s.util

import com.typesafe.config.ConfigFactory

import scala.util.Properties

trait Configurations {

  protected def config = ConfigFactory.load

  protected val twitterVersion = "1.1"

  protected val apiTwitterUrl = "https://api.twitter.com"
  protected val mediaTwitterUrl = "https://upload.twitter.com"
  protected val statusStreamingTwitterUrl = "https://stream.twitter.com"
  protected val userStreamingTwitterUrl = "https://userstream.twitter.com"

}

object TokensFromConfig extends Configurations {

  val consumerTokenKey = envVarOrConfig("TWITTER_CONSUMER_TOKEN_KEY", "twitter.consumer.key")
  val consumerTokenSecret = envVarOrConfig("TWITTER_CONSUMER_TOKEN_SECRET", "twitter.consumer.secret")

  val accessTokenKey = envVarOrConfig("TWITTER_ACCESS_TOKEN_KEY", "twitter.access.key")
  val accessTokenSecret = envVarOrConfig("TWITTER_ACCESS_TOKEN_SECRET", "twitter.access.secret")

  private def envVarOrConfig(envVar: String, configName: String): String = {
    try {
      Properties.envOrNone(envVar).getOrElse(config.getString(configName))
    } catch {
      case ex: Throwable =>
        val msg = s"[twitter4s] configuration missing: Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }
  }
}
