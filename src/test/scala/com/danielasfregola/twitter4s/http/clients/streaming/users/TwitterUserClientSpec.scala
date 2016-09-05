package com.danielasfregola.twitter4s.http.clients.streaming.users

import com.danielasfregola.twitter4s.entities.{DirectMessage, Tweet}
import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.http.clients.streaming.TwitterStreamingSpecContext
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http.Uri.Query
import spray.http.{HttpEntity, _}

import scala.io.Source

class TwitterUserClientSpec extends ClientSpec {

  class TwitterUserClientSpecContext extends TwitterStreamingSpecContext with TwitterUserClient

  "Twitter User Streaming Client" should {

    "start a filtered user stream" in new TwitterUserClientSpecContext {
      val result: Unit =
        when(getUserEvents(track = Seq("trending"))(dummyProcessing)).expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://userstream.twitter.com/1.1/user.json"
          request.uri.query === Query("stall_warnings=false&stringify_friend_ids=false&track=trending&with=followings")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
