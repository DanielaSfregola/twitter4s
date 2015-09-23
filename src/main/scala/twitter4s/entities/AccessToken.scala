package twitter4s.entities

import twitter4s.util.Configurations

case class AccessToken(key: String, secret: String)

object AccessToken extends Configurations {

  val AccessTokenFromConf = AccessToken(
    key = config.getString("twitter.access.key"),
    secret = config.getString("twitter.access.secret")
  )
}
