package com.danielasfregola.twitter4s.util

import com.typesafe.config.ConfigFactory

import scala.util.Properties

trait Configurations {

  val twitterVersion = Configurations.twitterVersion

  val apiTwitterUrl = Configurations.apiTwitterUrl
  val mediaTwitterUrl = Configurations.mediaTwitterUrl

  val statusStreamingTwitterUrl = Configurations.statusStreamingTwitterUrl
  val userStreamingTwitterUrl = Configurations.userStreamingTwitterUrl
  val siteStreamingTwitterUrl = Configurations.siteStreamingTwitterUrl

  lazy val consumerTokenKey = Configurations.consumerTokenKey
  lazy val consumerTokenSecret = Configurations.consumerTokenSecret

  lazy val accessTokenKey = Configurations.accessTokenKey
  lazy val accessTokenSecret = Configurations.accessTokenSecret
}

object Configurations {

  val config = ConfigFactory.load

  lazy val consumerTokenKey = envVarOrConfig("TWITTER_CONSUMER_TOKEN_KEY", "twitter.consumer.key")
  lazy val consumerTokenSecret = envVarOrConfig("TWITTER_CONSUMER_TOKEN_SECRET", "twitter.consumer.secret")

  lazy val accessTokenKey = envVarOrConfig("TWITTER_ACCESS_TOKEN_KEY", "twitter.access.key")
  lazy val accessTokenSecret = envVarOrConfig("TWITTER_ACCESS_TOKEN_SECRET", "twitter.access.secret")

  lazy val twitterVersion = "1.1"

  lazy val apiTwitterUrl = config.getString("twitter.rest.api")
  lazy val mediaTwitterUrl = config.getString("twitter.rest.media")
  lazy val statusStreamingTwitterUrl = config.getString("twitter.streaming.public")
  lazy val userStreamingTwitterUrl = config.getString("twitter.streaming.user")
  lazy val siteStreamingTwitterUrl = config.getString("twitter.streaming.site")

  private def envVarOrConfig(envVar: String, configName: String): String = {
    try {
      Properties.envOrNone(envVar).getOrElse(config.getString(configName))
    } catch {
      case ex: Throwable =>
        val msg =
          s"[twitter4s] configuration missing: Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }
  }
}
