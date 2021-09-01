package com.danielasfregola.twitter4s.http.clients.rest.v2.users

import akka.http.scaladsl.model.HttpMethods
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
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(userIds))
        }
        .respondWithOk
        .await
    }

    "request users with expansions" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      when(
        lookupUsers(
          ids = userIds,
          expansions = V2SpecQueryHelper.allUserExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserExpansionsQueryKeyValue(V2SpecQueryHelper.allUserExpansions),
            V2SpecQueryHelper.buildIdsQueryKeyValue(userIds)
          )
        }
        .respondWithOk
        .await
    }

    "request users with user fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      when(
        lookupUsers(
          ids = userIds,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(userIds),
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request users with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userIds = Seq("123", "456")
      when(
        lookupUsers(
          ids = userIds,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildIdsQueryKeyValue(userIds),
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
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
      when(
        lookupUser(
          id = userId,
          expansions = V2SpecQueryHelper.allUserExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserExpansionsQueryKeyValue(V2SpecQueryHelper.allUserExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request user with user fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      when(
        lookupUser(
          id = userId,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request user with tweet fields" in new TwitterUserLookupClientSpecContext {
      val userId = "123"
      when(
        lookupUser(
          id = userId,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/$userId"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
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
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUsernamesQueryKeyValue(usernames)
          )
        }
        .respondWithOk
        .await
    }

    "request users by usernames with expansions" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      when(
        lookupUsersByUsernames(
          usernames = usernames,
          expansions = V2SpecQueryHelper.allUserExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserExpansionsQueryKeyValue(V2SpecQueryHelper.allUserExpansions),
            V2SpecQueryHelper.buildUsernamesQueryKeyValue(usernames)
          )
        }
        .respondWithOk
        .await
    }

    "request users by usernames with user fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      when(
        lookupUsersByUsernames(
          usernames = usernames,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields),
            V2SpecQueryHelper.buildUsernamesQueryKeyValue(usernames)
          )
        }
        .respondWithOk
        .await
    }

    "request users by usernames with tweet fields" in new TwitterUserLookupClientSpecContext {
      val usernames = Seq("user1", "user2")
      when(
        lookupUsersByUsernames(
          usernames = usernames,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/2/users/by"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields),
            V2SpecQueryHelper.buildUsernamesQueryKeyValue(usernames)
          )
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
      when(
        lookupUserByUsername(
          username = username,
          expansions = V2SpecQueryHelper.allUserExpansions
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserExpansionsQueryKeyValue(V2SpecQueryHelper.allUserExpansions)
          )
        }
        .respondWithOk
        .await
    }

    "request user by username with user fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      when(
        lookupUserByUsername(
          username = username,
          userFields = V2SpecQueryHelper.allUserFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildUserFieldsQueryKeyValue(V2SpecQueryHelper.allUserFields)
          )
        }
        .respondWithOk
        .await
    }

    "request user by username with tweet fields" in new TwitterUserLookupClientSpecContext {
      val username = "user1"
      when(
        lookupUserByUsername(
          username = username,
          tweetFields = V2SpecQueryHelper.allTweetFields
        ))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/2/users/by/username/$username"
          V2SpecQueryHelper.extractEncodedQueryKeyValuesPairs(request.uri.rawQueryString.get) === Map(
            V2SpecQueryHelper.buildTweetFieldsQueryKeyValue(V2SpecQueryHelper.allTweetFields)
          )
        }
        .respondWithOk
        .await
    }

  }
}
