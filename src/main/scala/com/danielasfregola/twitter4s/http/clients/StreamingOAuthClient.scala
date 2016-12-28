package com.danielasfregola.twitter4s.http.clients

import akka.actor._
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.httpx.PipelineException
import spray.httpx.unmarshalling._

import scala.concurrent.Future
import scala.concurrent.duration._
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.entities.streaming.common._
import com.danielasfregola.twitter4s.entities.streaming.site._
import com.danielasfregola.twitter4s.entities.streaming.user._
import com.danielasfregola.twitter4s.exceptions.OldTwitterException

import scala.util.Success

private[twitter4s] trait StreamingOAuthClient extends OldOAuthClient {

  def streamingPipeline = { (requester: ActorRef, request: HttpRequest) =>
    request ~> (withOAuthHeader ~> logRequest ~> sendReceiveStream(requester) ~>
      logResponse(System.currentTimeMillis)(request) ~> unmarshalEmptyResponse)
  }

  def sendReceiveStream(requester: ActorRef): SendReceive = { request: HttpRequest =>
    val io = IO(Http)(system)
    val processor = system.actorOf(StreamingActor.props(requester))

    spray.client.pipelining.sendTo(io).withResponsesReceivedBy(processor)(request)

    val response = processor.ask(StreamingActor.FetchResponse)(Timeout(10.seconds))
    response onComplete {
      case Success(resp @ HttpResponse(_, _, _, _)) => resp
      case _ => processor ! PoisonPill; io ! PoisonPill
    }
    response.asInstanceOf[Future[HttpResponse]]
  }

  val commonStreamingMessageUnmarshaller: Unmarshaller[CommonStreamingMessage] =
    Unmarshaller.oneOf(
      json4sUnmarshaller[Tweet].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[DisconnectMessage].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[LimitNotice].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[LocationDeletionNotice].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[StatusDeletionNotice].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[StatusWithheldNotice].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[UserWithheldNotice].asInstanceOf[Unmarshaller[CommonStreamingMessage]],
      json4sUnmarshaller[WarningMessage].asInstanceOf[Unmarshaller[CommonStreamingMessage]]
    )

  val userStreamingMessageUnmarshaller: Unmarshaller[UserStreamingMessage] =
    Unmarshaller.oneOf(
      json4sUnmarshaller[DirectMessage].asInstanceOf[Unmarshaller[UserStreamingMessage]],
      json4sUnmarshaller[TwitterListEvent].asInstanceOf[Unmarshaller[UserStreamingMessage]],
      json4sUnmarshaller[TweetEvent].asInstanceOf[Unmarshaller[UserStreamingMessage]],
      json4sUnmarshaller[SimpleEvent].asInstanceOf[Unmarshaller[UserStreamingMessage]]
    )

  val siteStreamingMessageUnmarshaller: Unmarshaller[SiteStreamingMessage] =
    Unmarshaller.oneOf(
      json4sUnmarshaller[ControlMessage].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTweet].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopDirectMessage].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTwitterListEvent].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTweetEvent].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopSimpleEvent].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopWarningMessage].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTweetStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopDirectMessageStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTwitterListEventStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopTweetEventStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopSimpleEventStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]],
      json4sUnmarshaller[UserEnvelopWarningMessageStringified].asInstanceOf[Unmarshaller[SiteStreamingMessage]]
    )

  implicit val streamingMessageUnmarshaller: Unmarshaller[StreamingMessage] =
    Unmarshaller.oneOf(
      commonStreamingMessageUnmarshaller.asInstanceOf[Unmarshaller[StreamingMessage]],
      userStreamingMessageUnmarshaller.asInstanceOf[Unmarshaller[StreamingMessage]],
      siteStreamingMessageUnmarshaller.asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[UserEnvelopFriendsLists].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[UserEnvelopFriendsListsStringified].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[FriendsLists].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[FriendsListsStringified].asInstanceOf[Unmarshaller[StreamingMessage]]
    )

  def unmarshal[T: Unmarshaller]: HttpEntity ⇒ T = { response ⇒
    response.as[T] match {
      case Right(value) ⇒ value
      case Left(error: MalformedContent) ⇒
        throw new PipelineException(error.errorMessage, error.cause.orNull)
      case Left(error) ⇒ throw new PipelineException(error.toString)
    }
  }

  protected object StreamingActor {
    def props(requester: ActorRef) = Props(new StreamingActor(requester))
    object FetchResponse
  }

  protected class StreamingActor(requester: ActorRef) extends Actor with Stash {

    var response: Option[HttpResponse] = None
    var chunkBuffer: HttpData = HttpData.Empty

    def receive: Receive = {
      case StreamingActor.FetchResponse =>
        response match {
          case Some(rsp) => sender ! rsp
          case None => stash()
        }

      case ChunkedResponseStart(response: HttpResponse) =>
        this.response = Some(response)
        unstashAll()

      case ChunkedMessageEnd(extension: String, trailer: List[spray.http.HttpHeader]) =>
        context.stop(self)

      case MessageChunk(data: HttpData, extension: String) =>
        if (data.length >= 2 && data.slice(data.length - 2, data.length).asString == "\r\n") {
          if (data.length > 2) {
            val chunk = chunkBuffer +: data
            Future {
              try {
                val event = HttpEntity(ContentTypes.`application/json`, chunk) ~> unmarshal[StreamingMessage]
                StreamingUpdate(event)
              } catch {
                case t: Throwable =>
                  log.error(t, "Unable to parse statuses entity: [{}]", chunk.asString)
              }
            } onSuccess { case result: StreamingUpdate => requester ! result }
          }
          chunkBuffer = HttpData.Empty
        } else chunkBuffer = chunkBuffer +: data

      case HttpResponse(status, entity, _, _) if status.isFailure =>
        val msg = status match {
          case StatusCodes.Unauthorized => "Make sure your consumer and access tokens are correct"
          case _ => entity.asString
        }
        log.error(OldTwitterException(code = status, msg = msg), "While opening streaming connection")
        sender ! PoisonPill
    }
  }
}
