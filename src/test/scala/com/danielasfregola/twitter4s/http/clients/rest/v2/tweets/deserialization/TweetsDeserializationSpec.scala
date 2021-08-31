package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.deserialization

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.responses.{TweetResponse, TweetsResponse}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.TwitterTweetLookupClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.deserialization.fixtures.{
  TweetResponseFixture,
  TweetsResponseFixture
}

class TweetsDeserializationSpec extends ClientSpec {

  class TwitterTweetLookupClientSpecContext extends RestClientSpecContext with TwitterTweetLookupClient

  "Twitter Clients" should {

    "deserialize TweetResponse" in new TwitterTweetLookupClientSpecContext {
      val result: RatedData[TweetResponse] = when(lookupTweet("123"))
        .expectRequest(_ => {})
        .respondWithRated("/twitter/rest/v2/tweets/tweet.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetResponseFixture.fixture
    }

    "deserialize TweetsResponse" in new TwitterTweetLookupClientSpecContext {
      val result: RatedData[TweetsResponse] = when(lookupTweets(Seq("123")))
        .expectRequest(_ => {})
        .respondWithRated("/twitter/rest/v2/tweets/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === TweetsResponseFixture.fixture
    }
  }
}
