package twitter4s.entities

object TwitterEndpoints {

  private val baseUrl = "https://api.twitter.com"

  object OAuth {

    private val oAuthUrl = s"$baseUrl/oauth"

    val requestTokenUrl = s"$oAuthUrl/request_token"
    val authorizeTokenUrl = s"$oAuthUrl/authorize"
    val accessTokenUrl = s"$oAuthUrl/access_token"
  }

}



