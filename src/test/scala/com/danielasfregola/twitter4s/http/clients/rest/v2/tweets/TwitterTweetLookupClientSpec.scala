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
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(tweetIds)
          )
        }
        .respondWithOk
        .await
    }

    "request tweets with expansions" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      when(
        lookupTweets(
          ids = tweetIds,
          expansions = V2SpecQueryHelper.allTweetExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions),
            V2SpecQueryHelper.buildIdsQueryKeyValue(tweetIds)
          )
        }
        .respondWithOk
        .await
    }

    "request tweets with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      when(
        lookupTweets(
          ids = tweetIds,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(tweetIds),
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request tweets with user fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      when(
        lookupTweets(
          ids = tweetIds,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(tweetIds),
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request tweets with media fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      when(
        lookupTweets(
          ids = tweetIds,
          mediaFields = V2SpecQueryHelper.allMediaFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(tweetIds),
            V2SpecQueryHelper.buildMediaFieldsQueryKeyValue(V2SpecQueryHelper.allMediaFields)
          )
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
      when(
        lookupTweet(
          id = tweetId,
          expansions = V2SpecQueryHelper.allTweetExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request tweet with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      when(
        lookupTweet(
          id = tweetId,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request tweet with user fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      when(
        lookupTweet(
          id = tweetId,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request tweet with media fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      when(
        lookupTweet(
          id = tweetId,
          mediaFields = V2SpecQueryHelper.allMediaFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildMediaFieldsQueryKeyValue(V2SpecQueryHelper.allMediaFields)
          )
        }
        .respondWithOk
        .await
    }
  }
}
