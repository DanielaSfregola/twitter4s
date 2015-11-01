package twitter4s.http.clients

import spray.httpx.unmarshalling.{Deserializer => _}
import twitter4s.http.oauth.MediaOAuthProvider

trait MediaOAuthClient extends OAuthClient {

  override lazy val oauthProvider = new MediaOAuthProvider(consumerToken, accessToken)
}


