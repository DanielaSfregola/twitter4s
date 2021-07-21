package com.danielasfregola.twitter4s.http.clients.rest.v2.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.{Expansions => UserExpansions}
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.responses.{UserResponse, UsersResponse}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.fixtures.{UserResponseFixture, UsersResponseFixture}
import com.danielasfregola.twitter4s.http.clients.rest.v2.utils.V2SpecQueryHelper

class TwitterUserLookupClientSpec extends ClientSpec {

  class TwitterUserLookupClientSpecContext extends RestClientSpecContext with TwitterUserLookupClient

  "Twitter User Lookup Client" should {

    "lookup users" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123","456")
      val result: RatedData[UsersResponse] = when(lookupUsers(userIds))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildIdsParam(userIds))
        }
        .respondWithRated("/twitter/rest/v2/users/userlookup/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === UsersResponseFixture.fixture
    }

    "lookup users with expansions" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123","456")
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(lookupUsers(
        ids = userIds,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserExpansions(expansions),
            V2SpecQueryHelper.buildIdsParam(userIds)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup users with user fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123","456")
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupUsers(
        ids = userIds,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildIdsParam(userIds),
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup users with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123","456")
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupUsers(
        ids = userIds,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildIdsParam(userIds),
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val result: RatedData[UserResponse] = when(lookupUser(userId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/v2/users/userlookup/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === UserResponseFixture.fixture
    }

    "lookup user with expansions" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(lookupUser(
        id = userId,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserExpansions(expansions)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user with user fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupUser(
        id = userId,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupUser(
        id = userId,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup users by usernames" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1","user2")
      val result: RatedData[UsersResponse] = when(lookupUsersByUsernames(usernames))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(V2SpecQueryHelper.buildUsernamesParam(usernames))
        }
        .respondWithRated("/twitter/rest/v2/users/userlookup/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === UsersResponseFixture.fixture
    }

    "lookup users by usernames with expansions" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1","user2")
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(lookupUsersByUsernames(
        usernames = usernames,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserExpansions(expansions),
            V2SpecQueryHelper.buildUsernamesParam(usernames)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup users by usernames with user fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1","user2")
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupUsersByUsernames(
        usernames = usernames,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserFieldsParam(userFields),
            V2SpecQueryHelper.buildUsernamesParam(usernames)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup users by usernames with tweet fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1","user2")
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupUsersByUsernames(
        usernames = usernames,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields),
            V2SpecQueryHelper.buildUsernamesParam(usernames)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user by username" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val result: RatedData[UserResponse] = when(lookupUserByUsername(username))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/v2/users/userlookup/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === UserResponseFixture.fixture
    }

    "lookup user with expansions" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val expansions: Seq[UserExpansions] = V2SpecQueryHelper.allUserExpansions

      when(lookupUserByUsername(
        username = username,
        expansions = expansions
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserExpansions(expansions)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user by username with user fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val userFields: Seq[UserFields] = V2SpecQueryHelper.allUserFields

      when(lookupUserByUsername(
        username = username,
        userFields = userFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildUserFieldsParam(userFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

    "lookup user by username with tweet fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      val tweetFields: Seq[TweetFields] = V2SpecQueryHelper.allTweetFields

      when(lookupUserByUsername(
        username = username,
        tweetFields = tweetFields
      ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          request.uri.rawQueryString === Some(Seq(
            V2SpecQueryHelper.buildTweetFieldsParam(tweetFields)
          ).mkString("&"))
        }
        .respondWithOk
        .await
    }

  }
}
