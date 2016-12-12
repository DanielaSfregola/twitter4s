package com.danielasfregola.twitter4s.http.clients.streaming.sites

import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.http.clients.streaming.TwitterStreamingSpecContext
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http.HttpMethods
import spray.http.Uri.Query

class TwitterSiteClientSpec extends ClientSpec {

  class TwitterSiteClientSpecContext extends TwitterStreamingSpecContext with TwitterSiteClient

  "Twitter Site Streaming Client" should {

    "start a filtered site stream" in new TwitterSiteClientSpecContext {
      val result: Unit =
        when(getSiteEvents(languages = Seq(Language.Italian))(dummyProcessing)).expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://sitestream.twitter.com/1.1/site.json"
          request.uri.query === Query("language=it&stall_warnings=false&stringify_friend_ids=false&with=user")
        }.respondWithOk.await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
