package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.{StreamingUpdate, Tweet, LimitNotice}
import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.client.pipelining._
import spray.http.Uri.Query
import spray.http.{HttpData, MessageChunk, HttpMethods}

import scala.io.Source

class TwitterStreamingClientSpec extends ClientSpec {

  trait TwitterStreamingClientSpecContext extends ClientSpecContext with TwitterStreamingClient with ImplicitSender {
    override def sendReceiveStream(requester: ActorRef): SendReceive = sendReceive
  }

  "Twitter Streaming Client" should {
    //TODO: Add null case for when there are no search terms

    "start a stream with a query" in new TwitterStreamingClientSpecContext {
      val result: Unit =
        when(getStatusesFilter(track = Some("trending"))).expectRequest {
          request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/filter.json"
            request.uri.query === Query("track=trending")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "parse a stream of data and output the corresponding entities" in new TwitterStreamingClientSpecContext {
      val processor = system.actorOf(StreamingActor.props(self))

      Source.fromURL(getClass.getResource("/fixtures/streaming/status_filter.json")).getLines.foreach { line =>
        processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
      }

      val messages: Seq[StreamingUpdate] =
        (loadJsonAs[Seq[Tweet]]("/twitter/streaming/status_filter_tweets.json") ++
         loadJsonAs[Seq[LimitNotice]]("/twitter/streaming/status_filter_limit_notices.json"))
        .map (StreamingUpdate(_))

      expectMsgAllOf(messages: _*)
    }
  }
}
