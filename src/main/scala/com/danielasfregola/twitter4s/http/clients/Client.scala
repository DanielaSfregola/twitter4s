package com.danielasfregola.twitter4s.http.clients

import spray.httpx.PipelineException

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, UnmarshallerLifting}

import akka.actor.{Stash, Actor, Props, ActorRef}
import akka.pattern.{ ask, pipe }
import akka.io.IO
import akka.util.Timeout

import com.danielasfregola.twitter4s.http.unmarshalling.JsonSupport
import com.danielasfregola.twitter4s.providers.ActorRefFactoryProvider
import com.danielasfregola.twitter4s.util.ActorContextExtractor

trait Client extends JsonSupport with ActorContextExtractor with UnmarshallerLifting {
  self: ActorRefFactoryProvider =>

  implicit class RichHttpRequest(val request: HttpRequest) {
    def respondAs[T: Manifest]: Future[T] = sendReceiveAs[T](request)
    def respondAsStreamOf[T: Manifest](requester: ActorRef): Future[Unit] = sendReceiveStreamOf[T](requester, request)
  }

  def sendReceiveAs[T: FromResponseUnmarshaller](request: HttpRequest) =
    pipeline apply request

  def sendReceiveStreamOf[T: FromResponseUnmarshaller](processor: ActorRef, request: HttpRequest) =
    streamingPipeline[T] apply (processor, request)

  // TODO - logRequest, logResponse customisable?
  def pipeline[T: FromResponseUnmarshaller]: HttpRequest => Future[T]

  def streamingPipeline[T: FromResponseUnmarshaller]: (ActorRef, HttpRequest) => Future[Unit]

  def sendReceive = spray.client.pipelining.sendReceive

  def sendReceiveStream[T: FromResponseUnmarshaller](requester: ActorRef): SendReceive = { request: HttpRequest =>
    val io = IO(Http)(system)
    val processor = actorRefFactory.actorOf(StreamingActor.props[T](requester))
    spray.client.pipelining.sendTo(io).withResponsesReceivedBy(processor)(request)
    (processor.ask(StreamingActor.FetchResponse)(Timeout(10.seconds))).asInstanceOf[Future[HttpResponse]]
  }

  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info(s"${request.method} ${request.uri}")
    log.debug(s"${request.method} ${request.uri} | ${request.entity.asString} | ${request.headers} | $request")
    request
  }

  def logResponse(requestStartTime: Long)(implicit request: HttpRequest): HttpResponse => HttpResponse = { response =>
    val elapsed = System.currentTimeMillis - requestStartTime
    log.info(s"${request.method} ${request.uri} (${response.status}) | ${elapsed}ms")
    log.debug(s"${request.method} ${request.uri} (${response.status}) | ${response.entity.asString}")
    response
  }

  private object StreamingActor {
    def props[T: FromResponseUnmarshaller](requester: ActorRef) = Props(new StreamingActor[T](requester))
    object FetchResponse
  }

  private class StreamingActor[T: FromResponseUnmarshaller](requester: ActorRef) extends Actor with Stash {

    var response: Option[HttpResponse] = None
    var chunkBuffer: HttpData = HttpData.Empty

    def receive: Receive = {
      case StreamingActor.FetchResponse =>
        if (response.isDefined) sender ! response.get
        else stash()
      case ChunkedResponseStart(response: HttpResponse) =>
        this.response = Some(response)
        unstashAll()
      case MessageChunk(data: HttpData, extension: String) =>
        val length = chunkBuffer.length
        if(chunkBuffer.length >= 2 &&
            chunkBuffer.slice(chunkBuffer.length - 2, chunkBuffer.length).asString == "\r\n") {
          val chunk = chunkBuffer
          Future {
            try {
              // TODO: don't unmashall immediately, instead parse json and inspect the type of event
              // see https://dev.twitter.com/streaming/overview/messages-types
              response.get.withEntity(HttpEntity(ContentTypes.`application/json`, chunk)) ~> unmarshal[T]
            } catch {
              case e: PipelineException =>
                // Do nothing for now.
              case x: Throwable => throw x
            }
          } pipeTo requester
          chunkBuffer = data
        } else chunkBuffer = chunkBuffer +: data
    }
  }

}
