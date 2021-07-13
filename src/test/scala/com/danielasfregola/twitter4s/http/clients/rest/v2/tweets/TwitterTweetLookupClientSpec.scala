package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.responses.{TweetResponse, TweetsResponse}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.fixtures.{TweetResponseFixture, TweetsResponseFixture}
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterTweetLookupClientSpec extends ClientSpec {

  class TwitterTweetLookupClientSpecContext extends RestClientSpecContext with TwitterTweetLookupClient

  "Twitter Tweet Lookup Client" should {

    "lookup tweets" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val result: RatedData[TweetsResponse] = when(lookupTweets(tweetIds))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildIdsParam(tweetIds))
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }

    "lookup tweets with expansions" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val expansions = V2SpecQueryHelper.allTweetExpansions

      when(lookupTweets(
        ids = tweetIds,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetExpansions(expansions),
            V2SpecQueryHelper.buildIdsParam(tweetIds)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup tweets with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val tweetFields = V2SpecQueryHelper.allTweetFields

      when(lookupTweets(
        ids = tweetIds,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildIdsParam(tweetIds),
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup tweets with user fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val userFields = V2SpecQueryHelper.allUserFields

      when(lookupTweets(
        ids = tweetIds,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildIdsParam(tweetIds),
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup tweet" in new TwitterTweetLookupClientSpecContext {
      val result: RatedData[TweetResponse] = when(lookupTweet("123"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets/123"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweet.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetResponseFixture.fixture
    }

    "lookup tweet with expansions" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      val expansions = V2SpecQueryHelper.allTweetExpansions

      when(lookupTweet(
        id = tweetId,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildTweetExpansions(expansions))
        }
        .respondWithOk
        .await
    }

    "lookup tweet with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      val tweetFields = V2SpecQueryHelper.allTweetFields

      when(lookupTweet(
        id = tweetId,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildTweetFieldsParam(tweetFields))
        }
        .respondWithOk
        .await
    }

    "lookup tweet with user fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      val userFields = V2SpecQueryHelper.allUserFields

      when(lookupTweet(
        id = tweetId,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildUserFieldsParam(userFields))
        }
        .respondWithOk
        .await
    }
  }
}
