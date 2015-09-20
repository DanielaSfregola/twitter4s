package twitter4s.utils

import com.typesafe.config.{ConfigFactory, Config}

trait Configurations {
  def config: Config = ConfigFactory.load()

  val twitterUrl = "https://api.twitter.com"
  val apiVersion = "1.1"

}
