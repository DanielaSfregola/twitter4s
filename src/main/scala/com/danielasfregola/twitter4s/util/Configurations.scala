package com.danielasfregola.twitter4s.util

import com.danielasfregola.twitter4s.entities.enums.OAuthMode.{MixedAuth, OAuthMode, UseOAuth1, UseOAuthBearerToken}
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Properties
import scala.util.control.Exception.allCatch

object Configurations extends ConfigurationDetector {
  private def getAuthMode(): OAuthMode = {
    // First off - make sure we have SOMETHING to work with here
    if (bearerToken.isEmpty
        && consumerTokenKey.isEmpty
        && consumerTokenSecret.isEmpty
        && accessTokenKey.isEmpty
        && accessTokenSecret.isEmpty) {
      throw new RuntimeException(
        "[twitter4s] no environment variable or configuration settings were found for authenticating with Twitter's API.\nSee https://github.com/DanielaSfregola/twitter4s#usage for how to pass configuration")
    }

    // Getting here means we have something to work with. See what's empty out of everything we just checked
    if (bearerToken.isEmpty) {
      UseOAuth1 // No bearer token? Assume legacy oauth
    } else if (consumerTokenKey.isEmpty
               && consumerTokenSecret.isEmpty
               && accessTokenKey.isEmpty
               && accessTokenSecret.isEmpty) {
      UseOAuthBearerToken // No legacy auth info? Use bearer token.
    } else {
      MixedAuth // Use all the auth! (preference for legacy oauth, uses bearer token where needed)
    }
  }

  val config = ConfigFactory.load

  lazy val consumerTokenKey: Option[String] = envVarOrConfig("TWITTER_CONSUMER_TOKEN_KEY", "twitter.consumer.key")
  lazy val consumerTokenSecret: Option[String] =
    envVarOrConfig("TWITTER_CONSUMER_TOKEN_SECRET", "twitter.consumer.secret")

  lazy val accessTokenKey: Option[String] = envVarOrConfig("TWITTER_ACCESS_TOKEN_KEY", "twitter.access.key")
  lazy val accessTokenSecret: Option[String] = envVarOrConfig("TWITTER_ACCESS_TOKEN_SECRET", "twitter.access.secret")

  lazy val bearerToken: Option[String] = envVarOrConfig("TWITTER_BEARER_TOKEN", "twitter.bearer.token")
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

  protected def envVarOrConfig(envVar: String, configName: String): Option[String] =
    allCatch.opt(environmentVariable(envVar) getOrElse configuration(configName))

  protected def environmentVariable(name: String): Option[String] = Properties.envOrNone(name)

  protected def configuration(path: String): String = config.getString(path)

}
