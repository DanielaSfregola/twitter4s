package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider

import scala.concurrent.{ExecutionContext, Future}

private[twitter4s] trait OAuthClient extends CommonClient with RequestBuilding {

  def oauthProvider: OAuth1Provider

  def withOAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    for {
      authorizationHeader <- oauthProvider.oauth1Header(callback)(request, materializer)
    } yield request.withHeaders(request.headers :+ authorizationHeader)
  }

  def withSimpleOAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    for {
      authorizationHeader <- oauthProvider.oauth1Header(callback)(request.withEntity(HttpEntity.Empty), materializer)
    } yield request.withHeaders(request.headers :+ authorizationHeader)
  }

  override val Get = new OAuthRequestBuilder(GET)
  override val Post = new OAuthRequestBuilder(POST)
  override val Put = new OAuthRequestBuilder(PUT)
  override val Patch = new OAuthRequestBuilder(PATCH)
  override val Delete = new OAuthRequestBuilder(DELETE)
  override val Options = new OAuthRequestBuilder(OPTIONS)
  override val Head = new OAuthRequestBuilder(HEAD)

  private[twitter4s] class OAuthRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {

    def apply(uri: String, parameters: Parameters): HttpRequest =
      if (parameters.toString.nonEmpty) apply(s"$uri?$parameters") else apply(uri)

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

    def apply(uri: String, multipartFormData: Multipart.FormData)(implicit ec: ExecutionContext): HttpRequest =
      apply(Uri(uri), Some(multipartFormData))

  }

}
