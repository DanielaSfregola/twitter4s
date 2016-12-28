package com.danielasfregola.twitter4s.http.clients.streaming.users

import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.http.clients.streaming.TwitterStreamingSpecContext
import com.danielasfregola.twitter4s.util.OldClientSpec
import spray.http.HttpMethods
import spray.http.Uri.Query


class TwitterUserClientSpec extends OldClientSpec {

  class TwitterUserClientSpecContext extends TwitterStreamingSpecContext with TwitterUserClient

  "Twitter User Streaming OldClient" should {

    "start a filtered user stream" in new TwitterUserClientSpecContext {
      val result: Unit =
        when(userEvents(track = Seq("trending"), languages = Seq(Language.English))(dummyProcessing)).expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://userstream.twitter.com/1.1/user.json"
          request.uri.query === Query("language=en&stall_warnings=false&stringify_friend_ids=false&track=trending&with=followings")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
