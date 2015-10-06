package twitter4s.http.clients.friendships

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.{UserIdsStringified, UserIds}
import twitter4s.util.{ClientSpecContext, ClientSpec}

class TwitterFriendshipClientSpec extends ClientSpec {

  trait TwitterFriendshipClientSpecContext extends ClientSpecContext with TwitterFriendshipClient

  "Twitter Friendship Client" should {

    "get all blocked users" in new TwitterFriendshipClientSpecContext {
      val result: Seq[Long] = when(blockedUsers).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=false")
      }.respondWith("/twitter/friendships/blocked_users.json").await
      result === loadJsonAs[Seq[Long]]("/fixtures/friendships/blocked_users.json")
    }

    "get all blocked users stringified" in new TwitterFriendshipClientSpecContext {
      val result: Seq[String] = when(blockedUsersStringified).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=true")
      }.respondWith("/twitter/friendships/blocked_users_stringified.json").await
      result === loadJsonAs[Seq[String]]("/fixtures/friendships/blocked_users_stringified.json")
    }

    "get friends ids of a specific user by id" in new TwitterFriendshipClientSpecContext {
      val id = 2911461333L
      val result: UserIds = when(friendsById(id)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/friendships/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/friends_ids.json")
    }

    "get friends ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val name = "DanielaSfregola"
      val result: UserIds = when(friendsByName(name)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/friendships/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/friends_ids.json")
    }


    "get friends stringified ids of a specific user by id" in new TwitterFriendshipClientSpecContext {
      val id = 2911461333L
      val result: UserIdsStringified = when(friendsStringifiedById(id)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/friendships/friends_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/friends_ids_stringified.json")
    }

    "get friends stringified ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val name = "DanielaSfregola"
      val result: UserIdsStringified = when(friendsStringifiedByName(name)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/friendships/friends_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/friends_ids_stringified.json")
    }
  }
}
