package twitter4s.http.clients.users

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.User
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterUserClientSpec extends ClientSpec {

trait TwitterUserClientSpecContext extends ClientSpecContext with TwitterUserClient

  "Twitter User Client" should {

    "retrieve users" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(users("marcobonzanini", "odersky")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini,odersky")
      }.respondWith("/twitter/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/users/users.json")
    }

    "retrieve users by user ids" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(usersByUserId(19018614, 17765013)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.query === Query("include_entities=true&user_id=19018614,17765013")
      }.respondWith("/twitter/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/users/users.json")
    }
  }
}
