package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.{Expansions => TweetExpansions}
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.enums.rest.TimelineExclude
import com.danielasfregola.twitter4s.entities.v2.enums.rest.TimelineExclude.TimelineExclude
import com.danielasfregola.twitter4s.entities.v2.responses.TweetsResponse
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.fixtures.timelines.TweetsResponseFixture
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterTimelinesClientSpec extends ClientSpec {

  class TwitterTimelinesClientSpecContext extends RestClientSpecContext with TwitterTimelinesClient

  "Twitter Tweet Lookup Client" should {

    "lookup timelines" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val result: RatedData[TweetsResponse] = when(lookupTimeline(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/v2/tweets/timelines/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }

    "lookup timelines with expansions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val expansions: Seq[TweetExpansions] = V2SpecQueryHelper.allTweetExpansions

      when(lookupTimeline(
        userId = userId,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetExpansions(expansions)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup timelines with exclude" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val exclusions: Seq[TimelineExclude] = Seq(
        TimelineExclude.Replies,
        TimelineExclude.Retweets
      )

      when(lookupTimeline(
        userId = userId,
        exclude = exclusions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === Some("exclude=replies%2Cretweets")
        }
        .respondWithOk
        .await
    }

    "lookup timelines with tweet fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupTimeline(
        userId = userId,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup timelines with user fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupTimeline(
        userId = userId,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup timelines with media fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val mediaFields: Seq[MediaFields] = V2SpecQueryHelper.allMediaFields

      when(lookupTimeline(
        userId = userId,
        mediaFields = mediaFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/tweets"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildMediaFieldsParam(mediaFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup mentions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val result: RatedData[TweetsResponse] = when(lookupMentions(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/v2/tweets/timelines/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }

    "lookup mentions with expansions" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val expansions: Seq[TweetExpansions] = V2SpecQueryHelper.allTweetExpansions

      when(lookupMentions(
        userId = userId,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetExpansions(expansions)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup mentions with tweet fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupMentions(
        userId = userId,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup mentions with user fields" in new TwitterTimelinesClientSpecContext {
      val userId = "123"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupMentions(
        userId = userId,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId/mentions"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }
  }
}
