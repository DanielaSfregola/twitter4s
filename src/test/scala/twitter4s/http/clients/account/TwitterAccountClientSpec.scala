package twitter4s.http.clients.account

import spray.http.HttpMethods
import twitter4s.entities.Settings
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
  }

}
