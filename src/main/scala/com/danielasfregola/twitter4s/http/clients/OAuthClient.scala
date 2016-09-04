package com.danielasfregola.twitter4s.http.clients

import spray.client.pipelining._
import spray.http.HttpMethods._
import spray.http._
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, Deserializer => _}
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.http.oauth.OAuthProvider
import com.danielasfregola.twitter4s.providers.{ActorRefFactoryProvider, TokenProvider}

trait OAuthClient extends Client with TokenProvider with ActorRefFactoryProvider {

  protected lazy val oauthProvider = new OAuthProvider(consumerToken, accessToken)

  def pipeline[T: FromResponseUnmarshaller] = { implicit request =>
    request ~> (withOAuthHeader ~> logRequest ~> sendReceive ~> logResponse(System.currentTimeMillis) ~> unmarshalResponse[T])
  }

  protected def withOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request)
    request.withHeaders( request.headers :+ authorizationHeader )
  }

  def withSimpleOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request.withEntity(HttpEntity.Empty))
    request.withHeaders( request.headers :+ authorizationHeader )
  }

  private[twitter4s] val Get = new OAuthRequestBuilder(GET)
  private[twitter4s] val Post = new OAuthRequestBuilder(POST)
  private[twitter4s] val Put = new OAuthRequestBuilder(PUT)
  private[twitter4s] val Patch = new OAuthRequestBuilder(PATCH)
  private[twitter4s] val Delete = new OAuthRequestBuilder(DELETE)
  private[twitter4s] val Options = new OAuthRequestBuilder(OPTIONS)
  private[twitter4s] val Head = new OAuthRequestBuilder(HEAD)

  private[twitter4s] class OAuthRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {

    def apply(uri: String, parameters: Parameters): HttpRequest =
      if (!parameters.toString.isEmpty) apply(s"$uri?$parameters") else apply(uri)

    def apply(uri: String, content: Product): HttpRequest = {
      val data = toBodyAsEncodedParams(content)
      apply(uri, data, ContentType(MediaTypes.`application/x-www-form-urlencoded`))
    }

    def apply(uri: String, content: Product, contentType: ContentType): HttpRequest = {
      val data = toBodyAsParams(content)
      apply(uri, data, contentType)
    }

    def apply(uri: String, data: String, contentType: ContentType): HttpRequest =
      apply(uri).withEntity(HttpEntity(contentType, data))


    def apply(uri: String, multipartFormData: MultipartFormData): HttpRequest =
      apply(Uri(uri), Some(multipartFormData))

  }

}


