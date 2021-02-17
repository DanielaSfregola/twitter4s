package com.danielasfregola.twitter4s.util

import com.danielasfregola.twitter4s.entities.enums.OAuthMode.{MixedAuth, OAuthMode, UseOAuth1, UseOAuthBearerToken}
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Properties

object Configurations extends ConfigurationDetector {
  private def getAuthMode(): OAuthMode = {
    def isEmpty(x: String) = Option(x).forall(_.isEmpty)

    if (isEmpty(bearerToken)) {
      UseOAuth1 // No bearer token? Assume legacy oauth
    } else if (isEmpty(consumerTokenKey)
               && isEmpty(consumerTokenSecret)
               && isEmpty(accessTokenKey)
               && isEmpty(accessTokenSecret)) {
      UseOAuthBearerToken // No legacy auth info? Use bearer token.
    } else {
      MixedAuth // Use all the auth! (preference for legacy oauth, uses bearer token where needed)
    }
  }

  val config = ConfigFactory.load

  lazy val consumerTokenKey = envVarOrConfig("TWITTER_CONSUMER_TOKEN_KEY", "twitter.consumer.key")
  lazy val consumerTokenSecret = envVarOrConfig("TWITTER_CONSUMER_TOKEN_SECRET", "twitter.consumer.secret")

  lazy val accessTokenKey = envVarOrConfig("TWITTER_ACCESS_TOKEN_KEY", "twitter.access.key")
  lazy val accessTokenSecret = envVarOrConfig("TWITTER_ACCESS_TOKEN_SECRET", "twitter.access.secret")

  lazy val bearerToken = envVarOrConfig("TWITTER_BEARER_TOKEN", "twitter.bearer.token")
  lazy val authMode: OAuthMode = getAuthMode()

  lazy val twitterVersion = "1.1"
  lazy val twitterVersionNext = "2"

  lazy val apiTwitterUrl = config.getString("twitter.rest.api")
  lazy val mediaTwitterUrl = config.getString("twitter.rest.media")
  lazy val statusStreamingTwitterUrl = config.getString("twitter.streaming.public")
  lazy val userStreamingTwitterUrl = config.getString("twitter.streaming.user")
  lazy val siteStreamingTwitterUrl = config.getString("twitter.streaming.site")
}

trait ConfigurationDetector {

  def config: Config

  protected def envVarOrConfig(envVar: String, configName: String): String =
    try {
      environmentVariable(envVar) getOrElse configuration(configName)
    } catch {
      case _: Throwable =>
        val msg =
          s"[twitter4s] configuration missing: Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }

  protected def environmentVariable(name: String): Option[String] = Properties.envOrNone(name)

  protected def configuration(path: String): String = config.getString(path)

}
