package com.danielasfregola.twitter4s.util

import com.typesafe.config.{ConfigFactory, Config}

trait Configurations {
  def config: Config = ConfigFactory.load()

  val twitterVersion = "1.1"

  val apiTwitterUrl = "https://api.twitter.com"
  val mediaTwitterUrl = "https://upload.twitter.com"


}
