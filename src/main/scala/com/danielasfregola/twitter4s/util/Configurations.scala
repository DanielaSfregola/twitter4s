package com.danielasfregola.twitter4s.util

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Properties

object Configurations extends ConfigurationDetector {
  val config = ConfigFactory.load

  lazy val consumerTokenKey: Option[String] = envVarOrConfig("TWITTER_CONSUMER_TOKEN_KEY", "twitter.consumer.key")
  lazy val consumerTokenSecret: Option[String] =
    envVarOrConfig("TWITTER_CONSUMER_TOKEN_SECRET", "twitter.consumer.secret")

  lazy val accessTokenKey: Option[String] = envVarOrConfig("TWITTER_ACCESS_TOKEN_KEY", "twitter.access.key")
  lazy val accessTokenSecret: Option[String] = envVarOrConfig("TWITTER_ACCESS_TOKEN_SECRET", "twitter.access.secret")

  lazy val bearerToken: Option[String] = envVarOrConfig("TWITTER_BEARER_TOKEN", "twitter.bearer.token")

  lazy val twitterVersion = "1.1"
  lazy val twitterVersionNext = "2"

  lazy val apiTwitterUrl: String = config.getString("twitter.rest.api")
  lazy val mediaTwitterUrl: String = config.getString("twitter.rest.media")
  lazy val statusStreamingTwitterUrl: String = config.getString("twitter.streaming.public")
  lazy val userStreamingTwitterUrl: String = config.getString("twitter.streaming.user")
  lazy val siteStreamingTwitterUrl: String = config.getString("twitter.streaming.site")
}

trait ConfigurationDetector {

  def config: Config

  protected def envVarOrConfig(envVar: String, configName: String): Option[String] =
    try {
      Option(environmentVariable(envVar) getOrElse configuration(configName))
    } catch {
      case _: Throwable =>
        val msg =
          s"[twitter4s] configuration missing: Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }

  protected def environmentVariable(name: String): Option[String] = Properties.envOrNone(name)

  protected def configuration(path: String): String = config.getString(path)
}
