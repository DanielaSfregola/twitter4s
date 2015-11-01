package com.danielasfregola.twitter4s.http
package oauth

import spray.http.HttpRequest
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

class MediaOAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken) extends OAuthProvider(consumerToken, accessToken) {

  override def bodyParams(implicit request: HttpRequest): Map[String, String] = Map()

}



