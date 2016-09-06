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

private[twitter4s] trait StreamingOAuthClient extends OAuthClient {

  def streamingPipeline = { (requester: ActorRef, request: HttpRequest) =>
    request ~> (withOAuthHeader ~> logRequest ~> sendReceiveStream(requester) ~>
      logResponse(System.currentTimeMillis)(request) ~> unmarshalEmptyResponse)
  }

  def sendReceiveStream(requester: ActorRef): SendReceive = { request: HttpRequest =>
    val system: ActorSystem = actorRefFactory match {
      case x: ActorSystem  ⇒ x
      case x: ActorContext ⇒ x.system
    }

    val io = IO(Http)(system)
    val processor = actorRefFactory.actorOf(StreamingActor.props(requester))

    spray.client.pipelining.sendTo(io).withResponsesReceivedBy(processor)(request)

    val response = processor.ask(StreamingActor.FetchResponse)(Timeout(10.seconds))
    response onFailure { case (t: Throwable) =>
      processor ! PoisonPill
    }
    response.asInstanceOf[Future[HttpResponse]]
  }

  implicit val streamingMessageUnmarshaller: Unmarshaller[StreamingMessage] =
    Unmarshaller.oneOf(
      json4sUnmarshaller[Tweet].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[StatusDeletionNotice].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[LocationDeletionNotice].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[LimitNotice].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[StatusWithheldNotice].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[UserWithheldNotice].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[DisconnectMessage].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[WarningMessage].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[Event].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[FriendsLists].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[FriendsListsStringified].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[DirectMessage].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[UserEnvelop].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[UserEnvelopStringified].asInstanceOf[Unmarshaller[StreamingMessage]],
      json4sUnmarshaller[ControlMessage].asInstanceOf[Unmarshaller[StreamingMessage]]
    )

  implicit val eventTargetObjectUnmarshaller: Unmarshaller[EventTargetObject] =
    Unmarshaller.oneOf(
      json4sUnmarshaller[Tweet].asInstanceOf[Unmarshaller[EventTargetObject]],
      json4sUnmarshaller[TwitterList].asInstanceOf[Unmarshaller[EventTargetObject]],
      json4sUnmarshaller[DirectMessage].asInstanceOf[Unmarshaller[EventTargetObject]]
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
      case StreamingActor.FetchResponse => response match {
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
          if(data.length > 2) {
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
    }
  }
}
