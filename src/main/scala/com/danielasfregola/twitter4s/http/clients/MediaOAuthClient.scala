package com.danielasfregola.twitter4s.http.clients

import spray.httpx.unmarshalling.{Deserializer => _}
import com.danielasfregola.twitter4s.http.oauth.MediaOAuthProvider

trait MediaOAuthClient extends OAuthClient {

  override lazy val oauthProvider = new MediaOAuthProvider(consumerToken, accessToken)
}


