package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper
import java.time.Instant

class TwitterSearchTweetsClientSpec extends ClientSpec {

  class TwitterSearchTweetsClientSpecContext extends RestClientSpecContext with TwitterSearchTweetsClient

  "Twitter Search Tweets Client" should {

    "request search recent results" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchRecent(
          query =
            "-is:retweet has:geo (from:NWSNHC OR from:NHC_Atlantic OR from:NWSHouston OR from:NWSSanAntonio OR from:USGS_TexasRain OR from:USGS_TexasFlood OR from:JeffLindner1)",
          startTime = Some(Instant.ofEpochMilli(1630263600000L)), // Aug 29, 2021 07:00
          endTime = Some(Instant.ofEpochMilli(1630350000000L)), // Aug 30, 2021 06:00
          maxResults = Some(20),
          nextToken = Some("789"),
          sinceId = Some("1212"),
          untilId = Some("3434")
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/recent"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "-is%3Aretweet%20has%3Ageo%20%28from%3ANWSNHC%20OR%20from%3ANHC_Atlantic%20OR%20from%3ANWSHouston%20OR%20from%3ANWSSanAntonio%20OR%20from%3AUSGS_TexasRain%20OR%20from%3AUSGS_TexasFlood%20OR%20from%3AJeffLindner1%29",
            "start_time" -> "2021-08-29T19%3A00%3A00Z",
            "end_time" -> "2021-08-30T19%3A00%3A00Z",
            "max_results" -> "20",
            "next_token" -> "789",
            "since_id" -> "1212",
            "until_id" -> "3434"
          )
        }
        .respondWithOk
        .await
    }

    "request search recent results with expansions" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchRecent(
          query = "mountains",
          expansions = V2SpecQueryHelper.allTweetExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/recent"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request search recent results with media fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchRecent(
          query = "mountains",
          mediaFields = V2SpecQueryHelper.allMediaFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/recent"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildMediaFieldsQueryKeyValue(V2SpecQueryHelper.allMediaFields)
          )
        }
        .respondWithOk
        .await
    }

    "request search recent results with tweet fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchRecent(
          query = "mountains",
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/recent"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request search recent results with user fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchRecent(
          query = "mountains",
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/recent"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request search all results" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchAll(
          query =
            "-is:retweet has:geo (from:NWSNHC OR from:NHC_Atlantic OR from:NWSHouston OR from:NWSSanAntonio OR from:USGS_TexasRain OR from:USGS_TexasFlood OR from:JeffLindner1)",
          startTime = Some(Instant.ofEpochMilli(1630263600000L)), // Aug 29, 2021 07:00
          endTime = Some(Instant.ofEpochMilli(1630350000000L)), // Aug 30, 2021 06:00
          maxResults = Some(20),
          nextToken = Some("789"),
          sinceId = Some("1212"),
          untilId = Some("3434")
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/all"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "-is%3Aretweet%20has%3Ageo%20%28from%3ANWSNHC%20OR%20from%3ANHC_Atlantic%20OR%20from%3ANWSHouston%20OR%20from%3ANWSSanAntonio%20OR%20from%3AUSGS_TexasRain%20OR%20from%3AUSGS_TexasFlood%20OR%20from%3AJeffLindner1%29",
            "start_time" -> "2021-08-29T19%3A00%3A00Z",
            "end_time" -> "2021-08-30T19%3A00%3A00Z",
            "max_results" -> "20",
            "next_token" -> "789",
            "since_id" -> "1212",
            "until_id" -> "3434"
          )
        }
        .respondWithOk
        .await
    }

    "request search all results with expansions" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchAll(
          query = "mountains",
          expansions = V2SpecQueryHelper.allTweetExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/all"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildTweetExpansionsQueryKeyValue(V2SpecQueryHelper.allTweetExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request search all results with media fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchAll(
          query = "mountains",
          mediaFields = V2SpecQueryHelper.allMediaFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/all"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildMediaFieldsQueryKeyValue(V2SpecQueryHelper.allMediaFields)
          )
        }
        .respondWithOk
        .await
    }

    "request search all results with tweet fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchAll(
          query = "mountains",
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/all"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

    "request search all results with user fields" in new TwitterSearchTweetsClientSpecContext {
      when(
        searchAll(
          query = "mountains",
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/tweets/search/all"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            "query" -> "mountains",
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

  }
}
