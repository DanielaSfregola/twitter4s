package twitter4s.http.clients.friendships

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities._
import twitter4s.util.{ClientSpec, ClientSpecContext}

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
      val result: UserIds = when(friendsIdsPerUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/friendships/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/friends_ids.json")
    }

    "get friends ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(friendsIds("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/friendships/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/friends_ids.json")
    }


    "get friends stringified ids of a specific user by id" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(friendsPerUserIdStringified(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/friendships/friends_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/friends_ids_stringified.json")
    }

    "get friends stringified ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(friendsStringified("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/friendships/friends_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/friends_ids_stringified.json")
    }


    "get followers ids of a specific user by id" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(followersPerUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/friendships/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/followers_ids.json")
    }

    "get followers ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(followers("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/friendships/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/followers_ids.json")
    }


    "get followers stringified ids of a specific user by id" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(followersPerUserIdStringified(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/friendships/followers_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/followers_ids_stringified.json")
    }

    "get followers stringified ids of a specific user by name" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(followersStringified("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/friendships/followers_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/followers_ids_stringified.json")
    }

    "get incoming friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(incomingFriendships()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/friendships/incoming_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/incoming_friendships_ids.json")
    }

    "get incoming friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(incomingFriendshipsStringified()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/friendships/incoming_friendships_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/incoming_friendships_ids_stringified.json")
    }

    "get outgoing friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(outgoingFriendships()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/friendships/outgoing_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/outgoing_friendships_ids.json")
    }

    "get outgoing friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(outgoingFriendshipsStringified()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/friendships/outgoing_friendships_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/outgoing_friendships_ids_stringified.json")
    }

    "follow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(follow("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/follow.json")
    }

    "follow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&user_id=19018614")
      }.respondWith("/twitter/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/follow.json")
    }

    "unfollow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollow("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/unfollow.json")
    }

    "unfollow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollow(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/unfollow.json")
    }
    
    "enable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }
    
    "get a relationship by ids" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(relationship(2911461333L, 19018614L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_id=2911461333&target_id=19018614")
      }.respondWith("/twitter/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/relationship.json")
    }
    
    "get a relationship by screen names" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(relationship("DanielaSfregola", "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_screen_name=DanielaSfregola&target_screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/relationship.json")
    }

    "get friends of a user" in new TwitterFriendshipClientSpecContext {
      val result: Users = when(friends("DanielaSfregola", count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.query === Query("count=10&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/friendships/users.json").await
      result === loadJsonAs[Users]("/fixtures/friendships/users.json")
    }

    "get friends of a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Users = when(friendsPerUserId(2911461333L, count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.query === Query("count=10&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/friendships/users.json").await
      result === loadJsonAs[Users]("/fixtures/friendships/users.json")
    }
  }
}
