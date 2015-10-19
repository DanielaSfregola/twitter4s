package twitter4s.http.clients.help

import spray.http.HttpMethods
import twitter4s.LanguageDetails
import twitter4s.entities.Configuration
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterHelpClientSpec extends ClientSpec {

  trait TwitterHelpClientSpecContext extends ClientSpecContext with TwitterHelpClient

  "Twitter Help Client" should {

    "get twitter configuration" in new TwitterHelpClientSpecContext {
      val result: Configuration = when(configuration()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/help/configuration.json"
      }.respondWith("/twitter/help/configuration.json").await
      result === loadJsonAs[Configuration]("/fixtures/help/configuration.json")
    }

    "get supported languages" in new TwitterHelpClientSpecContext {
      val result: Seq[LanguageDetails] = when(supportedLanguages()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/help/languages.json"
      }.respondWith("/twitter/help/languages.json").await
      result === loadJsonAs[Seq[LanguageDetails]]("/fixtures/help/languages.json")
    }
  }
}
