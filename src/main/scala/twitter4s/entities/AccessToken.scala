package twitter4s.entities

import twitter4s.utils.ConfigProvider

case class AccessToken(key: String, secret: String)

object AccessToken extends ConfigProvider {

  val AccessTokenFromConf = AccessToken(
    key = config.getString("twitter.access.key"),
    secret = config.getString("twitter.access.secret")
  )
}
