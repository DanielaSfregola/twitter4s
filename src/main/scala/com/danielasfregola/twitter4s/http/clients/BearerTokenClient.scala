package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT}
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.model.{ContentType, HttpEntity, HttpMethod, HttpRequest, MediaTypes, Multipart, Uri}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.util.Configurations

import scala.concurrent.{ExecutionContext, Future}

private[twitter4s] trait BearerTokenClient extends CommonClient with RequestBuilding {

  // Perhaps in the future a client can be introduced to derive bearer tokens using oauth 2.0
  // https://developer.twitter.com/en/docs/authentication/oauth-2-0/bearer-tokens
  // For now we can just pull the token directly out of configuration
  private val bearerToken = Configurations.bearerToken

  def withBearerTokenHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    // I'm sure there's a less nasty way to do this?
    Future {
      request.withHeaders(request.headers :+ Authorization(OAuth2BearerToken(bearerToken)))
    }
  }

  override val Get = new BearerTokenRequestBuilder(GET)
  override val Post = new BearerTokenRequestBuilder(POST)
  override val Put = new BearerTokenRequestBuilder(PUT)
  override val Patch = new BearerTokenRequestBuilder(PATCH)
  override val Delete = new BearerTokenRequestBuilder(DELETE)
  override val Options = new BearerTokenRequestBuilder(OPTIONS)
  override val Head = new BearerTokenRequestBuilder(HEAD)

  private[twitter4s] class BearerTokenRequestBuilder(method: HttpMethod)
      extends RequestBuilder(method)
      with BodyEncoder {
    def apply(uri: String, parameters: Parameters): HttpRequest = {
      if (parameters.toString.nonEmpty) apply(s"$uri?$parameters")
      else apply(uri)
    }

    def apply(uri: String, content: Product): HttpRequest = {
      val data = toBodyAsEncodedParams(content)
      val contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`)
      apply(uri, data, contentType)
    }

    def asJson[A <: AnyRef](uri: String, content: A): HttpRequest = {
      val jsonData = org.json4s.native.Serialization.write(content)
      val contentType = ContentType(MediaTypes.`application/json`)
      apply(uri, jsonData, contentType)
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
