package twitter4s.http.clients.blocks

import spray.http.HttpMethods
import twitter4s.entities.Users
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterBlockClientSpec extends ClientSpec {

  trait TwitterBlockClientSpecContext extends ClientSpecContext with TwitterBlockClient

  "Twitter Block Client" should {

    "get blocked users" in new TwitterBlockClientSpecContext {
      val result: Users = when(blockedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/list.json"
      }.respondWith("/twitter/blocks/blocked_users.json").await
      result === loadJsonAs[Users]("/twitter/blocks/blocked_users.json")
    }
  }


}
