package twitter4s.entities

import twitter4s.utils.Configurations

case class ConsumerToken(key: String, secret: String)

object ConsumerToken extends Configurations {

  val ConsumerTokenFromConf = ConsumerToken(
    key = config.getString("twitter.consumer.key"),
    secret = config.getString("twitter.consumer.secret")
  )
}
