package com.danielasfregola.twitter4s.http.clients.streaming.statuses

import akka.http.scaladsl.model.{HttpEntity, HttpMethods}
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.{Coordinate, GeoBoundingBox}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterStatusClientSpec extends ClientSpec {

  class TwitterStatusClientSpecContext extends StreamingClientSpecContext with TwitterStatusClient

  "Twitter Status Streaming Client" should {

    "start a filtered status stream" in new TwitterStatusClientSpecContext {
      val locationsFilter = Seq(GeoBoundingBox(Coordinate(-122.75, 36.8), Coordinate(-121.75, 37.8)))
      val result: Unit =
        when(
          filterStatuses(follow = Seq(1L, 2L, 3L),
                         tracks = Seq("trending", "other"),
                         locations = locationsFilter,
                         languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing))
      when(
        filterStatuses(follow = Seq(1L, 2L, 3L),
                       tracks = Seq("trending", "other"),
                       languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/filter.json"
          request.entity === HttpEntity(
            `application/x-www-form-urlencoded`,
            "filter_level=none&follow=1%2C2%2C3&language=hu%2Cbn&locations=-122.75%2C36.8%2C-121.75%2C37.8&stall_warnings=false&track=trending%2Cother"
          )
        }
        .respondWithOk
        .await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a sample status stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(sampleStatuses(languages = Seq(Language.English))(dummyProcessing))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/sample.json"
            request.uri.rawQueryString === Some("filter_level=none&language=en&stall_warnings=false")
          }
          .respondWithOk
          .await
      result.isInstanceOf[Unit] should beTrue
    }

    "start a firehose status stream" in new TwitterStatusClientSpecContext {
      val result: Unit =
        when(firehoseStatuses(languages = Seq(Language.Hungarian, Language.Bengali))(dummyProcessing))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/firehose.json"
            request.uri.rawQueryString === Some("language=hu%2Cbn&stall_warnings=false")
          }
          .respondWithOk
          .await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
