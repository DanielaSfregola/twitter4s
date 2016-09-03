package com.danielasfregola.twitter4s.http.clients.streaming.statuses

import akka.actor.ActorRef
import akka.testkit.ImplicitSender
import com.danielasfregola.twitter4s.entities.streaming.{LimitNotice, StatusDeletionNotice, StreamingUpdate}
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.http.clients.streaming.TwitterStreamingSpecContext
import com.danielasfregola.twitter4s.listeners.TwitterStreamListener
import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.Uri.Query
import spray.http._

import scala.io.Source
import scala.reflect.ClassTag

class TwitterStatusClientSpec extends ClientSpec {

  trait TwitterStatusClientSpecContext extends TwitterStreamingSpecContext with TwitterStatusClient

  "Twitter Streaming Client" should {
    //TODO: Add null case for when there are no search terms

    "start a filtered stream with a query" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(getStatusesFilter[DummyListener](track = Some("trending"))).expectRequest {
          request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/filter.json"
            request.uri.query === Query("stall_warnings=false&track=trending")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a filtered stream with a query using POST method" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(postStatusesFilter[DummyListener](track = Some("trending"))).expectRequest {
          request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/filter.json"
            request.entity === HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`),
              "stall_warnings=false&track=trending")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a sample stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(getStatusesSample[DummyListener]()).expectRequest {
          request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/sample.json"
            request.uri.query === Query("stall_warnings=false")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "parse a stream of data and output the corresponding entities" in new TwitterStatusClientSpecContext {
      val processor = system.actorOf(StreamingActor.props(self))

      Source.fromURL(getClass.getResource("/fixtures/streaming/public.json")).getLines.foreach { line =>
        processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
      }

      val messages: Seq[StreamingUpdate] =
        (loadJsonAs[Seq[Tweet]]("/twitter/streaming/public_tweets.json") ++
         loadJsonAs[Seq[LimitNotice]]("/twitter/streaming/public_limit_notices.json") ++
         loadJsonAs[Seq[StatusDeletionNotice]]("/twitter/streaming/public_status_deletion_notices.json"))
        .map (StreamingUpdate(_))

      expectMsgAllOf(messages: _*)
    }
  }
}
