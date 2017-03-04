package com.danielasfregola.twitter4s.entities.authentication

import com.danielasfregola.twitter4s.http.serializers.FromMap

case class OAuthToken(oauth_token: String, oauth_token_secret: String, oauth_callback_confirmed: Boolean)

object OAuthToken {

  implicit object OAuthTokenFromMap extends FromMap[OAuthToken] {

    // example: oauth_token=Z6eEdO8MOmk394WozF5oKyuAv855l4Mlqo7hhlSLik&oauth_token_secret=Kd75W4OQfb2oJTV0vzGzeXftVAwgMnEK9MumzYcM&oauth_callback_confirmed=true
    def apply(m: Map[String, String]): Option[OAuthToken] = for {
      token <- m.get("oauth_token")
      secret <- m.get("oauth_token_secret")
      callbackConfirmed <- m.get("oauth_callback_confirmed")
    } yield OAuthToken(token, secret, callbackConfirmed)
  }
}
