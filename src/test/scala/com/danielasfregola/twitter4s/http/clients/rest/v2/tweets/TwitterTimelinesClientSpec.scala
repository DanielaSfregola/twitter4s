package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.v2.enums.rest.TimelineExclude
import com.danielasfregola.twitter4s.entities.v2.enums.rest.TimelineExclude.TimelineExclude
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterTimelinesClientSpec extends ClientSpec {

  class TwitterTimelinesClientSpecContext extends RestClientSpecContext with TwitterTimelinesClient

  "Twitter Tweet Lookup Client" should {

    "request timelines" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupTimeline(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === None
        }
        .respondWithOk
        .await
    }

    "request timelines with expansions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupTimeline(
        userId = userId,
        expansions = V2SpecQueryHelper.allTweetExpansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request timelines with exclude" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val exclusions: Seq[TimelineExclude] = Seq(
        TimelineExclude.Replies,
        TimelineExclude.Retweets
      )

      when(
        lookupTimeline(
          userId = userId,
          exclude = exclusions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "exclude" -> "replies%2Cretweets"
          )
        }
        .respondWithOk
        .await
    }

    "request timelines with tweet fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupTimeline(
        userId = userId,
        tweetFields = V2SpecQueryHelper.allTweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request timelines with user fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupTimeline(
        userId = userId,
        userFields = V2SpecQueryHelper.allUserFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request timelines with media fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupTimeline(
        userId = userId,
        mediaFields = V2SpecQueryHelper.allMediaFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildMediaFieldsQueryKeyValue(V2SpecQueryHelper.allMediaFields)
          )
        }
        .respondWithOk
        .await
    }

    "request mentions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupMentions(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          request.uri.rawQueryString === None
        }
        .respondWithOk
        .await
    }

    "request mentions with expansions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupMentions(
        userId = userId,
        expansions = V2SpecQueryHelper.allTweetExpansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request mentions with tweet fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupMentions(
        userId = userId,
        tweetFields = V2SpecQueryHelper.allTweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request mentions with user fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      when(lookupMentions(
        userId = userId,
        userFields = V2SpecQueryHelper.allUserFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }
  }
}
