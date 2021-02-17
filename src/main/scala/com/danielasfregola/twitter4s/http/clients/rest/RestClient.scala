package com.danielasfregola.twitter4s.http.clients.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.enums.OAuthMode.{MixedAuth, OAuthMode, UseOAuth1, UseOAuthBearerToken}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, RateLimit, RatedData}
import com.danielasfregola.twitter4s.http.clients.{BearerTokenClient, Client, OAuthClient}
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}

import scala.concurrent.{ExecutionContext, Future}

private[twitter4s] class RestClient(val authMode: OAuthMode)(implicit val system: ActorSystem)
    extends Client
    with RequestBuilding {
  var v1Client: OAuthClient = _
  var v2Client: BearerTokenClient = _

  def this(consumerToken: ConsumerToken, accessToken: AccessToken)(implicit system: ActorSystem) {
    this(UseOAuth1)
    this.v1Client = new OAuthClient(consumerToken, accessToken)
  }

  def this(bearerToken: String)(implicit system: ActorSystem) {
    this(UseOAuthBearerToken)
    v2Client = new BearerTokenClient(bearerToken)
  }

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {
    implicit val materializer = ActorMaterializer()
    implicit val ec = materializer.executionContext

    private def withHeader(callback: Option[String])(
        implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = {
      // Depending on our authMode, we may want to use a different helper function
      authMode match {
        case UseOAuth1           => v1Client.withOAuthHeader(callback)
        case UseOAuthBearerToken => v2Client.withBearerTokenHeader(callback)
        case MixedAuth           => v1Client.withOAuthHeader(callback)
        case _                   => v1Client.withOAuthHeader(callback)
      }
    }

    def respondAs[T: Manifest]: Future[T] =
      for {
        requestWithAuth <- withHeader(None)(materializer)(request)
        t <- sendReceiveAs[T](requestWithAuth)
      } yield t

    def respondAsRated[T: Manifest]: Future[RatedData[T]] =
      for {
        requestWithAuth <- withHeader(None)(materializer)(request)
        t <- sendReceiveAsRated[T](requestWithAuth)
      } yield t

    def sendAsFormData: Future[Unit] =
      for {
        requestWithAuth <- withHeader(None)(materializer)(request)
        _ <- sendIgnoreResponse(requestWithAuth)
      } yield ()

  }

  def sendIgnoreResponse(httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                   materializer: Materializer): Future[Unit] = {
    sendAndReceive(httpRequest, _ => Future.successful((): Unit))
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                           materializer: Materializer): Future[T] = {
    implicit val ec = materializer.executionContext
    implicit val jsonSerialization = serialization
    sendAndReceive(httpRequest, response => Unmarshal(response.entity).to[T])
  }

  def sendReceiveAsRated[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                                materializer: Materializer): Future[RatedData[T]] = {
    implicit val ec = materializer.executionContext
    implicit val jsonSerialization = serialization
    val unmarshallRated: HttpResponse => Future[RatedData[T]] = { response =>
      val rate = RateLimit(response.headers)
      val data = Unmarshal(response.entity).to[T]
      data.map(d => RatedData(rate, d))
    }
    sendAndReceive(httpRequest, unmarshallRated)
  }

  override val Get = new DynamicRequestBuilder(GET)
  override val Post = new DynamicRequestBuilder(POST)
  override val Put = new DynamicRequestBuilder(PUT)
  override val Patch = new DynamicRequestBuilder(PATCH)
  override val Delete = new DynamicRequestBuilder(DELETE)
  override val Options = new DynamicRequestBuilder(OPTIONS)
  override val Head = new DynamicRequestBuilder(HEAD)

  private[twitter4s] class DynamicRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {
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
