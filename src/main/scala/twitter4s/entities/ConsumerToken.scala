package twitter4s.entities

import twitter4s.utils.Configuration

case class ConsumerToken(key: String, secret: String)

object ConsumerToken extends Configuration {

  val ConsumerTokenFromConf = ConsumerToken(
    key = config.getString("twitter.consumer.key"),
    secret = config.getString("twitter.consumer.secret")
  )
}
