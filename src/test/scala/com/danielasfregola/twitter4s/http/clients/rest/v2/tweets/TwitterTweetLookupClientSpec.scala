package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterTweetLookupClientSpec extends ClientSpec {

  class TwitterTweetLookupClientSpecContext extends RestClientSpecContext with TwitterTweetLookupClient

  "Twitter Tweet Lookup Client" should {

    "request tweets" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")

      when(lookupTweets(tweetIds))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildIdsParam(tweetIds))
        }
        .respondWithOk
        .await
    }

    "request tweets with expansions" in new TwitterTweetLookupClientSpecContext {
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

    "request tweets with tweet fields" in new TwitterTweetLookupClientSpecContext {
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

    "request tweets with user fields" in new TwitterTweetLookupClientSpecContext {
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

    "request tweets with media fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val mediaFields = V2SpecQueryHelper.allMediaFields

      when(lookupTweets(
        ids = tweetIds,
        mediaFields = mediaFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildIdsParam(tweetIds),
            V2SpecQueryHelper.buildMediaFieldsParam(mediaFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request tweet" in new TwitterTweetLookupClientSpecContext {
      when(lookupTweet("123"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets/123"
          request.uri.rawQueryString === None
        }
        .respondWithOk
        .await
    }

    "request tweet with expansions" in new TwitterTweetLookupClientSpecContext {
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

    "request tweet with tweet fields" in new TwitterTweetLookupClientSpecContext {
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

    "request tweet with user fields" in new TwitterTweetLookupClientSpecContext {
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

    "request tweet with media fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      val mediaFields = V2SpecQueryHelper.allMediaFields

      when(lookupTweet(
        id = tweetId,
        mediaFields = mediaFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildMediaFieldsParam(mediaFields))
        }
        .respondWithOk
        .await
    }
  }
}
