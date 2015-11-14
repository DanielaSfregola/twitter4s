package com.danielasfregola.twitter4s.http.clients.account

import com.danielasfregola.twitter4s.http.{ClientSpec, ClientSpecContext}
import spray.http._
import com.danielasfregola.twitter4s.entities.enums.{Hour, TimeZone, ContributorType}
import com.danielasfregola.twitter4s.entities.{ProfileUpdate, User, Settings}
import com.danielasfregola.twitter4s.http.ClientSpecContext

class TwitterAccountClientSpec extends ClientSpec {

  trait TwitterAccountClientSpecContext extends ClientSpecContext with TwitterAccountClient

  "Twitter Account Client" should {

    "retrieve account settings" in new TwitterAccountClientSpecContext {
      val result: Settings = when(getSettings).expectRequest { request =>
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
      val result: Settings = when(updateSettings(
        allow_contributor_request = Some(ContributorType.All),
        sleep_time_enabled = Some(true),
        start_sleep_time = Some(Hour.TEN_PM),
        end_sleep_time = Some(Hour.SIX_AM),
        time_zone = Some(TimeZone.Europe_London))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/settings.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "allow_contributor_request=all&end_sleep_time=06&sleep_time_enabled=true&start_sleep_time=22&time_zone=Europe%2FLondon")
      }.respondWith("/twitter/account/settings.json").await
      result === loadJsonAs[Settings]("/fixtures/account/settings.json")
    }

    "update a profile name" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileName("Daniela Sfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "include_entities=true&name=Daniela+Sfregola&skip_status=false")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "update a profile url" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileUrl("http://danielasfregola.com")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "include_entities=true&skip_status=false&url=http%3A%2F%2Fdanielasfregola.com")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "update a profile description" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileDescription("Nice description here")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "description=Nice+description+here&include_entities=true&skip_status=false")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "update a profile location" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileLocation("London, UK")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "include_entities=true&location=London%2C+UK&skip_status=false")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "update a profile link color" in new TwitterAccountClientSpecContext {
      val result: User = when(updateProfileLinkColor("0000FF")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "include_entities=true&profile_link_color=0000FF&skip_status=false")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "update a profile" in new TwitterAccountClientSpecContext {
      val profile = ProfileUpdate(url = Some("http://danielasfregola.com"))
      val result: User = when(updateProfile(profile)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/update_profile.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "include_entities=true&skip_status=false&url=http%3A%2F%2Fdanielasfregola.com")
      }.respondWith("/twitter/account/user.json").await
      result === loadJsonAs[User]("/fixtures/account/user.json")
    }

    "remove a profile banner" in new TwitterAccountClientSpecContext {
      val result: Unit = when(removeProfileBanner).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/account/remove_profile_banner.json"
      }.respondWithOk.await
      result === (())
    }
  }

}
