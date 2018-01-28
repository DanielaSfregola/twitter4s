package com.danielasfregola.twitter4s.http.clients.rest.help

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterHelpClientSpec extends ClientSpec {

  class TwitterHelpClientSpecContext extends RestClientSpecContext with TwitterHelpClient

  "Twitter Help Client" should {

    "get twitter configuration" in new TwitterHelpClientSpecContext {
      val result: RatedData[Configuration] = when(configuration())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/help/configuration.json"
        }
        .respondWithRated("/twitter/rest/help/configuration.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Configuration]("/fixtures/rest/help/configuration.json")
    }

    "get supported languages" in new TwitterHelpClientSpecContext {
      val result: RatedData[Seq[LanguageDetails]] = when(supportedLanguages())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/help/languages.json"
        }
        .respondWithRated("/twitter/rest/help/languages.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LanguageDetails]]("/fixtures/rest/help/languages.json")
    }

    "get twitter privacy policy" in new TwitterHelpClientSpecContext {
      val result: RatedData[PrivacyPolicy] = when(privacyPolicy())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/help/privacy.json"
        }
        .respondWithRated("/twitter/rest/help/privacy.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[PrivacyPolicy]("/fixtures/rest/help/privacy.json")
    }

    "get twitter terms of service" in new TwitterHelpClientSpecContext {
      val result: RatedData[TermsOfService] = when(termsOfService())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/help/tos.json"
        }
        .respondWithRated("/twitter/rest/help/tos.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[TermsOfService]("/fixtures/rest/help/tos.json")
    }
  }
}
