package twitter4s.http.clients.account

import spray.http.{MediaTypes, ContentType, HttpEntity, HttpMethods}
import twitter4s.entities.enums.ContributorType
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

    "update account settings" in new TwitterAccountClientSpecContext {
      val result: Settings = when(updateSettings(allow_contributor_request = Some(ContributorType.ALL))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/settings.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "allow_contributor_request=all")
      }.respondWith("/twitter/account/settings.json").await
      result === loadJsonAs[Settings]("/fixtures/account/settings.json")
    }
  }

}
