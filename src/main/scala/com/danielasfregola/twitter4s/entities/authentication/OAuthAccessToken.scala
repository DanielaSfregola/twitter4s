package com.danielasfregola.twitter4s.entities.authentication

import com.danielasfregola.twitter4s.entities.AccessToken
import com.danielasfregola.twitter4s.http.serializers.FromMap

final case class OAuthAccessToken(accessToken: AccessToken, user_id: Long, screen_name: String)

object OAuthAccessToken {

  implicit val oAuthAccessTokenFromMap = new FromMap[OAuthAccessToken] {

    def apply(m: Map[String, String]): Option[OAuthAccessToken] =
      for {
        key <- m.get("oauth_token")
        secret <- m.get("oauth_token_secret")
        userId <- m.get("user_id")
        screenName <- m.get("screen_name")
      } yield OAuthAccessToken(AccessToken(key, secret), userId.toLong, screenName)
  }
}
