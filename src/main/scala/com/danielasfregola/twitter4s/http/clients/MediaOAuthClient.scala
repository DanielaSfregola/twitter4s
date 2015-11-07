package com.danielasfregola.twitter4s.http.clients

import spray.http.HttpMethods._
import spray.http._
import spray.httpx.unmarshalling.{Deserializer => _}
import com.danielasfregola.twitter4s.http.oauth.MediaOAuthProvider

trait MediaOAuthClient extends OAuthClient {

  override lazy val oauthProvider = new MediaOAuthProvider(consumerToken, accessToken)

  override val Get = new MediaOAuthRequestBuilder(GET)
  override val Post = new MediaOAuthRequestBuilder(POST)
  override val Put = new MediaOAuthRequestBuilder(PUT)
  override val Patch = new MediaOAuthRequestBuilder(PATCH)
  override val Delete = new MediaOAuthRequestBuilder(DELETE)
  override val Options = new MediaOAuthRequestBuilder(OPTIONS)
  override val Head = new MediaOAuthRequestBuilder(HEAD)

  class MediaOAuthRequestBuilder(method: HttpMethod) extends OAuthRequestBuilder(method) {

    override def apply(uri: String, content: Product): HttpRequest = {
      val data = toBodyAsParams(content)
      apply(uri, data, ContentType(MediaTypes.`multipart/form-data`))
    }

  }
}


