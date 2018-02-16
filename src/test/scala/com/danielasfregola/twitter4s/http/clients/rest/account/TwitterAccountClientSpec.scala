package com.danielasfregola.twitter4s.http.clients.rest.account

import akka.http.scaladsl.model._
import com.danielasfregola.twitter4s.entities.enums.{ContributorType, Hour, TimeZone}
import com.danielasfregola.twitter4s.entities.{ProfileUpdate, RatedData, Settings, User}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterAccountClientSpec extends ClientSpec {

  class TwitterAccountClientSpecContext extends RestClientSpecContext with TwitterAccountClient

  "Twitter Account Client" should {

    "retrieve account settings" in new TwitterAccountClientSpecContext {
      val result: RatedData[Settings] = when(settings)
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/account/settings.json"
        }
        .respondWithRated("/twitter/rest/account/settings.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Settings]("/fixtures/rest/account/settings.json")
    }

    "verify credentials" in new TwitterAccountClientSpecContext {
      val result: RatedData[User] = when(verifyCredentials(include_email = true))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/account/verify_credentials.json"
        }
        .respondWithRated("/twitter/rest/account/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update account settings" in new TwitterAccountClientSpecContext {
      val result: Settings = when(
        updateSettings(
          allow_contributor_request = Some(ContributorType.All),
          sleep_time_enabled = Some(true),
          start_sleep_time = Some(Hour.TEN_PM),
          end_sleep_time = Some(Hour.SIX_AM),
          time_zone = Some(TimeZone.Europe_London)
        ))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/settings.json"
          request.entity === HttpEntity(
            `application/x-www-form-urlencoded`,
            "allow_contributor_request=all&end_sleep_time=06&sleep_time_enabled=true&start_sleep_time=22&time_zone=Europe%2FLondon"
          )
        }
        .respondWith("/twitter/rest/account/settings.json")
        .await
      result === loadJsonAs[Settings]("/fixtures/rest/account/settings.json")
    }

    "update a profile name" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileName("Daniela Sfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "include_entities=true&name=Daniela%20Sfregola&skip_status=false")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update a profile url" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileUrl("http://danielasfregola.com"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "include_entities=true&skip_status=false&url=http%3A%2F%2Fdanielasfregola.com")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update a profile description" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileDescription("Nice description here"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "description=Nice%20description%20here&include_entities=true&skip_status=false")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update a profile location" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileLocation("London, UK"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "include_entities=true&location=London%2C%20UK&skip_status=false")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update a profile link color" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileLinkColor("0000FF"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "include_entities=true&profile_link_color=0000FF&skip_status=false")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "update a profile" in new TwitterAccountClientSpecContext {
      val profile = ProfileUpdate(url = Some("http://danielasfregola.com"))
      val result: User = when(updateProfile(profile))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
          request.entity === HttpEntity(`application/x-www-form-urlencoded`,
                                        "include_entities=true&skip_status=false&url=http%3A%2F%2Fdanielasfregola.com")
        }
        .respondWith("/twitter/rest/account/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/account/user.json")
    }

    "remove a profile banner" in new TwitterAccountClientSpecContext {
      val result: Unit = when(removeProfileBanner)
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account/remove_profile_banner.json"
        }
        .respondWithOk
        .await
      result.isInstanceOf[Unit] should beTrue
    }
  }

}
