package com.danielasfregola.twitter4s.http.clients

import scala.concurrent.Future
import scala.util.Try
import org.json4s.native.Serialization
import spray.client.pipelining._
import spray.http.HttpMethods._
import spray.http._
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, Deserializer => _}
import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterException}
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.http.oauth.OAuthProvider
import com.danielasfregola.twitter4s.providers.{ActorRefFactoryProvider, TokenProvider}

import akka.actor.ActorRef

trait OAuthClient extends Client with TokenProvider with ActorRefFactoryProvider {

  protected lazy val oauthProvider = new OAuthProvider(consumerToken, accessToken)

  def pipeline[T: FromResponseUnmarshaller] = { implicit request =>
    request ~> (withOAuthHeader ~> logRequest ~> sendReceive ~> logResponse(System.currentTimeMillis) ~> unmarshalResponse[T])
  }

  def streamingPipeline[T: FromResponseUnmarshaller] = { (requester: ActorRef, request: HttpRequest) =>
    request ~> (withOAuthHeader ~> logRequest ~> sendReceiveStream[T](requester) ~>
      logResponse(System.currentTimeMillis)(request) ~> unmarshalResponse)
  }

  protected def withOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request)
    request.withHeaders( request.headers :+ authorizationHeader )
  }

  def withSimpleOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request.withEntity(HttpEntity.Empty))
    request.withHeaders( request.headers :+ authorizationHeader )
  }
  
  protected def unmarshalResponse[T: FromResponseUnmarshaller]: HttpResponse => T = { hr =>
    hr.status.isSuccess match {
      case true => hr ~> unmarshal[T]
      case false =>
        val errors = Try {
          Serialization.read[Errors](hr.entity.asString)
        } getOrElse { Errors() }
        throw new TwitterException(hr.status, errors)
    }
  }

  protected def unmarshalResponse: HttpResponse => Unit = { hr =>
    hr.status.isSuccess match {
      case true => Unit
      case false =>
        val errors = Try {
          Serialization.read[Errors](hr.entity.asString)
        } getOrElse { Errors() }
        throw new TwitterException(hr.status, errors)
    }
  }

  val Get = new OAuthRequestBuilder(GET)
  val Post = new OAuthRequestBuilder(POST)
  val Put = new OAuthRequestBuilder(PUT)
  val Patch = new OAuthRequestBuilder(PATCH)
  val Delete = new OAuthRequestBuilder(DELETE)
  val Options = new OAuthRequestBuilder(OPTIONS)
  val Head = new OAuthRequestBuilder(HEAD)

  class OAuthRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {

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


