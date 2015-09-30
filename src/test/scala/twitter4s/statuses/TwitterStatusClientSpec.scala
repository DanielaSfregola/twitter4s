package twitter4s.statuses

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.Tweet
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterStatusClientSpec extends ClientSpec {

  trait TwitterStatusClientSpecContext extends ClientSpecContext with TwitterStatusClient

  "Twitter Status Client" should {

    "perform a mentions timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(mentionsTimeline(count = 10)).expectRequest { request =>
                    request.method === HttpMethods.GET
                    request.uri.endpoint === "https://api.twitter.com/1.1/statuses/mentions_timeline.json"
                    request.uri.query === Query("contributor_details=false&count=10&include_entities=true&trim_user=false")
                  }.respondWith("/twitter/mentions_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/mentions_timeline.json")
    }


    "perform a user timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(userTimeline(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/user_timeline.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_rts=true&trim_user=false")
      }.respondWith("/twitter/user_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/user_timeline.json")
    }

    "perform a home timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(homeTimeline(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/home_timeline.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_entities=true&trim_user=false")
      }.respondWith("/twitter/home_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/home_timeline.json")
    }

    "perform a retweets of me request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(retweetsOfMe(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/retweets_of_me.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_entities=true&trim_user=false")
      }.respondWith("/twitter/retweets_of_me.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/retweets_of_me.json")
    }

    "perform a retweets request" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Seq[Tweet] = when(retweets(id, count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/statuses/retweets/$id.json"
        request.uri.query === Query("count=10&trim_user=false")
      }.respondWith("/twitter/retweets.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/retweets.json")
    }

    "perform a show tweet request" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Tweet = when(show(id)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/statuses/show.json"
        request.uri.query === Query("id=648866645855879168&include_entities=true&include_my_retweet=false&rim_user=false")
      }.respondWith("/twitter/show.json").await
      result === loadJsonAs[Tweet]("/fixtures/show.json")
    }
  }
}

