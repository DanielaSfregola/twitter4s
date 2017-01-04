package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider
import com.danielasfregola.twitter4s.providers.{ActorSystemProvider, TokenProvider}

import scala.concurrent.Future

private[twitter4s] trait OAuthClient extends CommonClient with TokenProvider with ActorSystemProvider with RequestBuilding {

  protected lazy val oauthProvider = new OAuth2Provider(consumerToken, accessToken)

  def withOAuthHeader: HttpRequest => Future[HttpRequest] = { request =>
    for {
      authorizationHeader <- oauthProvider.oauth2Header(request)
    } yield request.withHeaders( request.headers :+ authorizationHeader )
  }

  def withSimpleOAuthHeader: HttpRequest => Future[HttpRequest] = { request =>
    for {
      authorizationHeader <- oauthProvider.oauth2Header(request.withEntity(HttpEntity.Empty))
    } yield request.withHeaders( request.headers :+ authorizationHeader )
  }

  override val Get = new OAuthRequestBuilder(GET)
  override val Post = new OAuthRequestBuilder(POST)
  override val Put = new OAuthRequestBuilder(PUT)
  override val Patch = new OAuthRequestBuilder(PATCH)
  override val Delete = new OAuthRequestBuilder(DELETE)
  override val Options = new OAuthRequestBuilder(OPTIONS)
  override val Head = new OAuthRequestBuilder(HEAD)

  class OAuthRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {

    def apply(uri: String, parameters: Parameters): HttpRequest =
      if (!parameters.toString.isEmpty) apply(s"$uri?$parameters") else apply(uri)

    def apply(uri: String, content: Product): HttpRequest = {
      val data = toBodyAsEncodedParams(content)
      val contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`)
      apply(uri, data, contentType)
    }

    def apply(uri: String, content: Product, contentType: ContentType): HttpRequest = {
      val data = toBodyAsParams(content)
      apply(uri, data, contentType)
    }

    def apply(uri: String, data: String, contentType: ContentType): HttpRequest =
      apply(uri).withEntity(HttpEntity(data).withContentType(contentType))

    def apply(uri: String, multipartFormData: Multipart.FormData): HttpRequest =
      apply(Uri(uri), Some(multipartFormData))

  }

}


