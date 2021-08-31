package com.danielasfregola.twitter4s.http.clients.rest.v2.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.UserExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterUserLookupClientSpec extends ClientSpec {

  class TwitterUserLookupClientSpecContext extends RestClientSpecContext with TwitterUserLookupClient

  "Twitter User Lookup Client" should {

    "request users" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      when(lookupUsers(userIds))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildIdsParam(userIds))
        }
        .respondWithOk
        .await
    }

    "request users with expansions" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(
        lookupUsers(
          ids = userIds,
          expansions = expansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserExpansions(expansions),
              V2SpecQueryHelper.buildIdsParam(userIds)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request users with user fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(
        lookupUsers(
          ids = userIds,
          userFields = userFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildIdsParam(userIds),
              V2SpecQueryHelper.buildUserFieldsParam(userFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request users with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(
        lookupUsers(
          ids = userIds,
          tweetFields = tweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildIdsParam(userIds),
              V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      when(lookupUser(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === None
        }
        .respondWithOk
        .await
    }

    "request user with expansions" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(
        lookupUser(
          id = userId,
          expansions = expansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserExpansions(expansions)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user with user fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(
        lookupUser(
          id = userId,
          userFields = userFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserFieldsParam(userFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(
        lookupUser(
          id = userId,
          tweetFields = tweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request users by usernames" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      when(lookupUsersByUsernames(usernames))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildUsernamesParam(usernames))
        }
        .respondWithOk
        .await
    }

    "request users by usernames with expansions" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(
        lookupUsersByUsernames(
          usernames = usernames,
          expansions = expansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserExpansions(expansions),
              V2SpecQueryHelper.buildUsernamesParam(usernames)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request users by usernames with user fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(
        lookupUsersByUsernames(
          usernames = usernames,
          userFields = userFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserFieldsParam(userFields),
              V2SpecQueryHelper.buildUsernamesParam(usernames)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request users by usernames with tweet fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(
        lookupUsersByUsernames(
          usernames = usernames,
          tweetFields = tweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildTweetFieldsParam(tweetFields),
              V2SpecQueryHelper.buildUsernamesParam(usernames)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user by username" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      when(lookupUserByUsername(username))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === None
        }
        .respondWithOk
        .await
    }

    "request user with expansions" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(
        lookupUserByUsername(
          username = username,
          expansions = expansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserExpansions(expansions)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user by username with user fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(
        lookupUserByUsername(
          username = username,
          userFields = userFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildUserFieldsParam(userFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "request user by username with tweet fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(
        lookupUserByUsername(
          username = username,
          tweetFields = tweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(
            Seq(
              V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
            ).mkString("&"))
        }
        .respondWithOk
        .await
    }

  }
}
