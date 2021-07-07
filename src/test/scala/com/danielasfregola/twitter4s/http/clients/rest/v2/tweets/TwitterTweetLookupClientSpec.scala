package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.responses.{TweetResponse, TweetsResponse}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.fixtures.{TweetResponseFixture, TweetsResponseFixture}

class TwitterTweetLookupClientSpec extends ClientSpec {

  class TwitterTweetLookupClientSpecContext extends RestClientSpecContext with TwitterTweetLookupClient

  "Twitter Tweet Lookup Client" should {

    "lookup tweets" in new TwitterTweetLookupClientSpecContext {
      val result: RatedData[TweetsResponse] = when(lookupTweets(Seq("123", "456")))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some("ids=123%2C456")
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }

    "lookup tweets with expansions" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val expansions = Seq(
        TweetExpansions.`Entities.Mentions.Username`,
        TweetExpansions.InReplyToUser,
        TweetExpansions.`ReferencedTweets.Id`,
        TweetExpansions.`ReferencedTweets.Id.AuthorId`
      )

      val result: RatedData[TweetsResponse] = when(lookupTweets(
        ids = tweetIds,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(
            "expansions=entities.mentions.username%2Cin_reply_to_user_id%2Creferenced_tweets.id%2Creferenced_tweets.id.author_id&ids=123%2C456"
          )
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }

    "lookup tweets with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetIds = Seq("123", "456")
      val tweetFields = Seq(
        TweetFields.Attachments,
        TweetFields.AuthorId,
        TweetFields.ContextAnnotations,
        TweetFields.ConversationId,
        TweetFields.CreatedAt,
        TweetFields.Entities,
        TweetFields.Geo,
        TweetFields.Id,
        TweetFields.InReplyToUserId,
        TweetFields.Lang,
        TweetFields.NonPublicMetrics,
        TweetFields.PublicMetrics,
        TweetFields.OrganicMetrics,
        TweetFields.PromotedMetrics,
        TweetFields.PossiblySensitive,
        TweetFields.ReferencedTweets,
        TweetFields.ReplySettings,
        TweetFields.Source,
        TweetFields.Text,
        TweetFields.Withheld
      )

      val result: RatedData[TweetsResponse] = when(lookupTweets(
        ids = tweetIds,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/tweets"
          request.uri.rawQueryString === Some(
            "ids=123%2C456&tweet%2Efields=attachments%2Cauthor_id%2Ccontext_annotations%2Cconversation_id%2Ccreated_at%2Centities%2Cgeo%2Cid%2Cin_reply_to_user_id%2Clang%2Cnon_public_metrics%2Cpublic_metrics%2Corganic_metrics%2Cpromoted_metrics%2Cpossibly_sensitive%2Creferenced_tweets%2Creply_settings%2Csource%2Ctext%2Cwithheld"
          )
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
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
      val expansions = Seq(
        TweetExpansions.`Entities.Mentions.Username`,
        TweetExpansions.InReplyToUser,
        TweetExpansions.`ReferencedTweets.Id`,
        TweetExpansions.`ReferencedTweets.Id.AuthorId`
      )

      val result: RatedData[TweetResponse] = when(lookupTweet(
        id = tweetId,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(
            "expansions=entities.mentions.username%2Cin_reply_to_user_id%2Creferenced_tweets.id%2Creferenced_tweets.id.author_id"
          )
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweet.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetResponseFixture.fixture
    }

    "lookup tweet with tweet fields" in new TwitterTweetLookupClientSpecContext {
      val tweetId = "123"
      val tweetFields = Seq(
        TweetFields.Attachments,
        TweetFields.AuthorId,
        TweetFields.ContextAnnotations,
        TweetFields.ConversationId,
        TweetFields.CreatedAt,
        TweetFields.Entities,
        TweetFields.Geo,
        TweetFields.Id,
        TweetFields.InReplyToUserId,
        TweetFields.Lang,
        TweetFields.NonPublicMetrics,
        TweetFields.PublicMetrics,
        TweetFields.OrganicMetrics,
        TweetFields.PromotedMetrics,
        TweetFields.PossiblySensitive,
        TweetFields.ReferencedTweets,
        TweetFields.ReplySettings,
        TweetFields.Source,
        TweetFields.Text,
        TweetFields.Withheld
      )

      val result: RatedData[TweetResponse] = when(lookupTweet(
        id = tweetId,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/$tweetId"
          request.uri.rawQueryString === Some(
            "tweet%2Efields=attachments%2Cauthor_id%2Ccontext_annotations%2Cconversation_id%2Ccreated_at%2Centities%2Cgeo%2Cid%2Cin_reply_to_user_id%2Clang%2Cnon_public_metrics%2Cpublic_metrics%2Corganic_metrics%2Cpromoted_metrics%2Cpossibly_sensitive%2Creferenced_tweets%2Creply_settings%2Csource%2Ctext%2Cwithheld"
          )
        }
        .respondWithRated("/twitter/rest/v2/tweets/tweetlookup/tweet.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetResponseFixture.fixture
    }
  }
}
