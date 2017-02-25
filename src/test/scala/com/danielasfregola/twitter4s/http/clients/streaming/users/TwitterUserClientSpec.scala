package com.danielasfregola.twitter4s.http.clients.streaming.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.helpers.streaming.ClientSpec

class TwitterUserClientSpec extends ClientSpec {

  class TwitterUserClientSpecContext extends ClientSpecContext with TwitterUserClient

  "Twitter User Streaming Client" should {

    "start a filtered user stream" in new TwitterUserClientSpecContext {
      val result: Unit =
        when(userEvents(tracks = Seq("trending"), languages = Seq(Language.English))(dummyProcessing)).expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://userstream.twitter.com/1.1/user.json"
          request.uri.queryString() === Some("language=en&stall_warnings=false&stringify_friend_ids=false&track=trending&with=followings")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
