package com.danielasfregola.twitter4s.util

import com.typesafe.config.{ConfigFactory, Config}

trait Configurations {
  protected def config: Config = ConfigFactory.load()

  protected val twitterVersion = "1.1"

  protected val apiTwitterUrl = "https://api.twitter.com"
  protected val mediaTwitterUrl = "https://upload.twitter.com"


}
