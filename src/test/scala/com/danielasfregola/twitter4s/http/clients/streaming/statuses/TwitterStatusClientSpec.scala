package com.danielasfregola.twitter4s.http.clients.streaming.statuses

import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.http.clients.streaming.TwitterStreamingSpecContext
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http.Uri.Query
import spray.http.{HttpEntity, _}

class TwitterStatusClientSpec extends ClientSpec {

  class TwitterStatusClientSpecContext extends TwitterStreamingSpecContext with TwitterStatusClient

  "Twitter Status Streaming Client" should {

    "start a filtered status stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(getStatusesFilter(track = Seq("trending"), languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing)).expectRequest {
          request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/filter.json"
            request.entity === HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`),
              "language=hu%2Cbn&stall_warnings=false&track=trending")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a sample status stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(getStatusesSample(languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing)).expectRequest {
          request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/sample.json"
            request.uri.query === Query("language=hu%2Cbn&stall_warnings=false")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a firehose status stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(getStatusesFirehose(languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing)).expectRequest {
          request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/firehose.json"
            request.uri.query === Query("language=hu%2Cbn&stall_warnings=false")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
