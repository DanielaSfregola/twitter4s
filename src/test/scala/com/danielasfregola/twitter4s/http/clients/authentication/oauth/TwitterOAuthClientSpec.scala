package com.danielasfregola.twitter4s.http.clients.authentication.oauth

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.authentication.OAuthToken
import com.danielasfregola.twitter4s.entities.enums.AccessType
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterOAuthClientSpec extends ClientSpec {

  class TwitterAccountClientSpecContext extends AuthenticationClientSpecContext with TwitterOAuthClient

  "Twitter OAuth Client" should {

    "request a token with static callback" in new TwitterAccountClientSpecContext {
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

    "request a token with dynamic callback and access type" in new TwitterAccountClientSpecContext {
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
  }
}
