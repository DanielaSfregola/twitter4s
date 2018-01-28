package com.danielasfregola.twitter4s.entities.authentication

import com.danielasfregola.twitter4s.http.serializers.FromMap

final case class OAuthRequestToken(token: RequestToken, oauth_callback_confirmed: Boolean)

final case class RequestToken(key: String, secret: String)

object OAuthRequestToken {

  implicit val oAuthRequestTokenFromMap = new FromMap[OAuthRequestToken] {

    def apply(m: Map[String, String]): Option[OAuthRequestToken] =
      for {
        key <- m.get("oauth_token")
        secret <- m.get("oauth_token_secret")
        callbackConfirmed <- m.get("oauth_callback_confirmed")
      } yield OAuthRequestToken(RequestToken(key, secret), toBoolean(callbackConfirmed))
  }
}
