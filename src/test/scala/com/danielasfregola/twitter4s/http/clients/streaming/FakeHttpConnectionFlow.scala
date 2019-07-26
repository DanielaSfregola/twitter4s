package com.danielasfregola.twitter4s.http.clients.streaming

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, model}
import akka.http.scaladsl.model.HttpEntity.{ChunkStreamPart, Chunked}
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode
import com.danielasfregola.twitter4s.entities.streaming.common.{DisconnectMessage, DisconnectMessageInfo}
import org.json4s.native.Serialization

import scala.concurrent.{Await, Future}
import scala.util.Success

trait FakeHttpConnectionFlow {

  def getRequest: HttpRequest
  implicit val actorSystem: ActorSystem
  implicit val mat: Materializer



  protected lazy val connectionFlow: Flow[HttpRequest, HttpResponse, NotUsed] = {


    val msg = DisconnectMessage(DisconnectMessageInfo(DisconnectionCode.Normal, "test", "test"))
    implicit val formats = org.json4s.DefaultFormats
    implicit val ec = mat.executionContext

    val jsoned: String = Serialization.write(msg)

    Flow[HttpRequest]
      .map(
        x => {
          x.discardEntityBytes().future.onComplete { done =>
            println("Entity discarded completely!")
          }
          HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, jsoned + ByteString("\r\n")))
        }
      )
  }

  def serialise(value: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close()
    stream.toByteArray
  }

}
