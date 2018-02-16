package com.danielasfregola.twitter4s.http.clients.authentication.oauth

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.AccessToken
import com.danielasfregola.twitter4s.entities.authentication.{OAuthAccessToken, OAuthRequestToken, RequestToken}
import com.danielasfregola.twitter4s.entities.enums.AccessType
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterOAuthClientSpec extends ClientSpec {

  class TwitterOAuthClientSpecContext extends AuthenticationClientSpecContext with TwitterOAuthClient {
    val token = RequestToken("my-oauth-token", "my-oauth-secret")
  }

  "Twitter OAuth Client" should {

    "request a oauth request token with static callback" in new TwitterOAuthClientSpecContext {
      val result: OAuthRequestToken = when(requestToken())
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/oauth/request_token"
          request.uri.rawQueryString === None
        }
        .respondWith("/twitter/authentication/request_token.txt")
        .await
      val expectedResult = OAuthRequestToken(token = RequestToken(key = "Z6eEdO8MOmk394WozF5oKyuAv855l4Mlqo7hhlSLik",
                                                                  secret = "Kd75W4OQfb2oJTV0vzGzeXftVAwgMnEK9MumzYcM"),
                                             oauth_callback_confirmed = true)
      result === expectedResult
    }

    "request a oauth request token with dynamic callback and access type" in new TwitterOAuthClientSpecContext {
      val result: OAuthRequestToken = when(requestToken(Some("http://my.callback"), Some(AccessType.Read)))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/oauth/request_token"
          request.uri.rawQueryString === Some("x_auth_access_type=read")
        }
        .respondWith("/twitter/authentication/request_token.txt")
        .await
      val expectedResult = OAuthRequestToken(token = RequestToken(key = "Z6eEdO8MOmk394WozF5oKyuAv855l4Mlqo7hhlSLik",
                                                                  secret = "Kd75W4OQfb2oJTV0vzGzeXftVAwgMnEK9MumzYcM"),
                                             oauth_callback_confirmed = true)
      result === expectedResult
    }

    "generate an authentication url" in new TwitterOAuthClientSpecContext {
      val url: String = authenticateUrl(token = token, force_login = true, screen_name = Some("DanielaSfregola"))
      val params = "oauth_token=my-oauth-token&force_login=true&screen_name=DanielaSfregola"
      url === s"https://api.twitter.com/oauth/authenticate?$params"
    }

    "generate an authorization url" in new TwitterOAuthClientSpecContext {
      val url: String = authorizeUrl(token = token, force_login = true, screen_name = Some("DanielaSfregola"))
      val params = "oauth_token=my-oauth-token&force_login=true&screen_name=DanielaSfregola"
      url === s"https://api.twitter.com/oauth/authorize?$params"
    }

    "request an access token for x_auth authentication" in new TwitterOAuthClientSpecContext {
      val result: OAuthAccessToken = when(accessToken("twitterapi", "my-secret-password"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/oauth/access_token"
          request.uri.rawQueryString === Some(
            "x_auth_mode=client_auth&x_auth_password=my-secret-password&x_auth_username=twitterapi")
        }
        .respondWith("/twitter/authentication/access_token.txt")
        .await
      val expectedResult = OAuthAccessToken(
        accessToken = AccessToken(key = "6253282-eWudHldSbIaelX7swmsiHImEL4KinwaGloHANdrY",
                                  secret = "2EEfA6BG3ly3sR3RjE0IBSnlQu4ZrUzPiYKmrkVU"),
        user_id = 6253282,
        screen_name = "twitterapi"
      )
      result === expectedResult
    }

    "request an access token for oauth authentication" in new TwitterOAuthClientSpecContext {
      val result: OAuthAccessToken = when(accessToken(token = token, oauth_verifier = "my-token-verifier"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/oauth/access_token"
          request.uri.rawQueryString === Some("oauth_token=my-oauth-token&oauth_verifier=my-token-verifier")
        }
        .respondWith("/twitter/authentication/access_token.txt")
        .await
      val expectedResult = OAuthAccessToken(
        accessToken = AccessToken(key = "6253282-eWudHldSbIaelX7swmsiHImEL4KinwaGloHANdrY",
                                  secret = "2EEfA6BG3ly3sR3RjE0IBSnlQu4ZrUzPiYKmrkVU"),
        user_id = 6253282,
        screen_name = "twitterapi"
      )
      result.accessToken === expectedResult.accessToken
      result.user_id === expectedResult.user_id
      result.screen_name === expectedResult.screen_name
      result === expectedResult
    }
  }
}
