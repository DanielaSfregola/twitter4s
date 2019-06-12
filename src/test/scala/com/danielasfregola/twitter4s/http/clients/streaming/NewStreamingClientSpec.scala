package com.danielasfregola.twitter4s.http.clients.streaming

import akka.http.scaladsl.model.{HttpEntity, _}
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestProbe
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.helpers.{ClientSpec, TestActorSystem}

import scala.io.Codec

class NewStreamingClientSpec extends TestActorSystem with ClientSpec {

  abstract class ClientSpecContext extends StreamingClientSpecContext {

    val ns = new StreamingClientNew(consumerToken, accessToken)
    import ns._

    implicit val exampleRequest: HttpRequest = Get("an-example-request", ContentTypes.`application/json`)

    def buildResponse(resourcePath: String): HttpResponse = {
      val contentType = ContentTypes.`application/json`
      val streamInput = getClass.getResourceAsStream(resourcePath)
      val data = scala.io.Source.fromInputStream(streamInput)(Codec.UTF8).getLines.filter(_.nonEmpty)
      val chunks = data.map(d => HttpEntity.ChunkStreamPart(s"$d\r\n")).toList
      HttpResponse(entity = HttpEntity.Chunked(contentType, Source(chunks)))
    }

    def redirectMessages: PartialFunction[StreamingMessage, Unit] = {
      case msg => transport.ref ! StreamingUpdate(msg)
    }

    def readStreamUpdatesAs[T <: StreamingMessage: Manifest](path: String): Seq[T] =
      loadJsonAs[Seq[T]](path)
  }

  "StreamingClient" should {

    "return a stream for an http response" in new ClientSpecContext {
      val response = buildResponse("/twitter/streaming/common/tweets.json")
      val source = ns.processHttpResponse(response, killSwitch)
      val probe = TestProbe()
      val cancellable = source.to(Sink.actorRef(probe.ref, "completed")).run()
      val messages = readStreamUpdatesAs[Tweet]("/fixtures/streaming/common/tweets.json")
      probe.expectMsgAllOf(messages: _*)
    }

  }
}
