package twitter4s.http.clients.account

import spray.http.HttpMethods
import twitter4s.entities.{User, Settings}
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterAccountClientSpec extends ClientSpec {

  trait TwitterAccountClientSpecContext extends ClientSpecContext with TwitterAccountClient

  "Twitter Friend Client" should {

    "retrieve account settings" in new TwitterAccountClientSpecContext {
      val result: Settings = when(settings).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/account/settings.json"
      }.respondWith("/twitter/account/settings.json").await
      result === loadJsonAs[Settings]("/fixtures/account/settings.json")
    }

    "verify credentials" in new TwitterAccountClientSpecContext {
      val result: User = when(verifyCredentials(include_email = true)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/account/verify_credentials.json"
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }
  }

}
