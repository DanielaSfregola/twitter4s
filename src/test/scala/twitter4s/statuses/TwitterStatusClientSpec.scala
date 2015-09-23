package twitter4s.statuses

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.Status
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterStatusClientSpec extends ClientSpec {

  trait TwitterStatusClientSpecContext extends ClientSpecContext with TwitterStatusClient

  "Twitter Status Client" should {

    "perform a mentions timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Status] = when(mentionsTimeline(count = Some(10))).expectRequest{ request =>
                    request.method === HttpMethods.GET
                    request.uri.endpoint === "https://api.twitter.com/1.1/statuses/mentions_timeline.json"
                    request.uri.query === Query("contributor_details=false&count=10&include_entities=true&trim_user=false")
                  }.respondWith("/twitter/mentions_timeline.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/mentions_timeline.json")
    }

    "perform a user timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Status] = when(userTimeline(count = Some(10))).expectRequest{ request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/user_timeline.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_rts=true&trim_user=false")
      }.respondWith("/twitter/user_timeline.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/user_timeline.json")
    }
  }
}

