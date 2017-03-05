package com.danielasfregola.twitter4s.http.clients.authentication.oauth

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.authentication.OAuthToken
import com.danielasfregola.twitter4s.entities.enums.AccessType
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterOAuthClientSpec extends ClientSpec {

  class TwitterOAuthClientSpecContext extends AuthenticationClientSpecContext with TwitterOAuthClient

  "Twitter OAuth Client" should {

    "request a token with static callback" in new TwitterOAuthClientSpecContext {
      val result: OAuthToken = when(requestToken()).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/oauth/request_token"
        request.uri.queryString() === None
      }.respondWith("/twitter/authentication/request_token.txt").await
      val expectedResult = OAuthToken(
        oauth_token = "Z6eEdO8MOmk394WozF5oKyuAv855l4Mlqo7hhlSLik",
        oauth_token_secret = "Kd75W4OQfb2oJTV0vzGzeXftVAwgMnEK9MumzYcM",
        oauth_callback_confirmed = true)
      result === expectedResult
    }

    "request a token with dynamic callback and access type" in new TwitterOAuthClientSpecContext {
      val result: OAuthToken = when(requestToken(Some("http://my.callback"), Some(AccessType.Read))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/oauth/request_token"
        request.uri.queryString() === Some("x_auth_access_type=read")
      }.respondWith("/twitter/authentication/request_token.txt").await
      val expectedResult = OAuthToken(
        oauth_token = "Z6eEdO8MOmk394WozF5oKyuAv855l4Mlqo7hhlSLik",
        oauth_token_secret = "Kd75W4OQfb2oJTV0vzGzeXftVAwgMnEK9MumzYcM",
        oauth_callback_confirmed = true)
      result === expectedResult
    }

    "generate an authentication url" in new TwitterOAuthClientSpecContext {
      val url: String = authenticateUrl(oauth_token = "my-auth-token", force_login = true, screen_name = Some("DanielaSfregola"))
      val params = "oauth_token=my-auth-token&force_login=true&screen_name=DanielaSfregola"
      url === s"https://api.twitter.com/oauth/authenticate?$params"
    }

    "generate an authorization url" in new TwitterOAuthClientSpecContext {
      val url: String = authorizeUrl(oauth_token = "my-auth-token", force_login = true, screen_name = Some("DanielaSfregola"))
      val params = "oauth_token=my-auth-token&force_login=true&screen_name=DanielaSfregola"
      url === s"https://api.twitter.com/oauth/authorize?$params"
    }
  }
}
