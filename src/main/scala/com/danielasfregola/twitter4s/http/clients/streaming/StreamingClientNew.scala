package com.danielasfregola.twitter4s.http.clients.streaming

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Framing, Source}
import akka.stream.{ActorMaterializer, SharedKillSwitch}
import akka.util.ByteString
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.util.Try

private[twitter4s] class StreamingClientNew(val consumerToken: ConsumerToken, val accessToken: AccessToken)(
    implicit val system: ActorSystem)
    extends OAuthClient {

  override def withLogRequest: Boolean = true
  override def withLogRequestResponse: Boolean = true

  implicit val materializer = ActorMaterializer()
  implicit val ec = materializer.executionContext

  def preProcessing(): Unit = ()

  lazy val oauthProvider = new OAuth1Provider(consumerToken, Some(accessToken))

  def getStream(request: HttpRequest, killSwitch: SharedKillSwitch): Future[Source[StreamingMessage, NotUsed]] = {
    withOAuthHeader(None)(materializer)(request).map(_ => requestIntoStream(killSwitch)(request))
  }

  private def requestIntoStream(killSwitch: SharedKillSwitch)(implicit request: HttpRequest) = {
    Source
      .single(request)
      .via(connection)
      .flatMapConcat(response => processHttpResponse(response, killSwitch))
  }

  def processHttpResponse(httpResponse: HttpResponse, killSwitch: SharedKillSwitch) = {
    httpResponse match {
      case response if response.status.isSuccess =>
        processBody(response, killSwitch)
      case failureResponse =>
        val statusCode = failureResponse.status
        val msg = "Stream could not be opened"
        parseFailedResponse(failureResponse).map(ex => logger.error(s"$msg: $ex"))
        Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
    }
  }

  def processBody[T: Manifest, A](
      response: HttpResponse,
      killSwitch: SharedKillSwitch
  ): Source[StreamingMessage, Any] =
    response.entity.withoutSizeLimit.dataBytes
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
      .filter(_.nonEmpty)
      .via(killSwitch.flow)
      .map(data => unmarshalStream(data))
      .filter(_.isSuccess)
      .map(_.get)

  private def unmarshalStream[T <: StreamingMessage: Manifest](data: ByteString): Try[StreamingMessage] = {
    val json = data.utf8String
    Try(Serialization.read[StreamingMessage](json))
  }

}

case class StreamResult()
case class TwitterStreamingEvent()
