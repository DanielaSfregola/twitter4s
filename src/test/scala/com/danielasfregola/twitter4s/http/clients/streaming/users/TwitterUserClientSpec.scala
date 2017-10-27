package com.danielasfregola.twitter4s.http.clients.streaming.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{GeoBoundingBox, Coordinate}
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterUserClientSpec extends ClientSpec {

  class TwitterUserClientSpecContext extends StreamingClientSpecContext with TwitterUserClient

  "Twitter User Streaming Client" should {

    "start a filtered user stream" in new TwitterUserClientSpecContext {
      val locationsFilter = Seq(
        GeoBoundingBox(Coordinate(-122.75, 36.8), Coordinate(-121.75, 37.8)),
        GeoBoundingBox(Coordinate(-74, 40), Coordinate(-73, 41))
      )
      val result: Unit =
        when(userEvents(tracks = Seq("trending"),
                        languages = Seq(Language.English),
                        locations = locationsFilter)(dummyProcessing))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://userstream.twitter.com/1.1/user.json"
            request.uri.queryString() === Some("filter_level=none&language=en&locations=-122.75,36.8,-121.75,37.8,-74.0,40.0,-73.0,41.0&stall_warnings=false&stringify_friend_ids=false&track=trending&with=followings")
          }
          .respondWithOk
          .await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
