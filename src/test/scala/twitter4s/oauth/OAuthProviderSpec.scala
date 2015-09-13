package twitter4s.oauth

import org.specs2.mutable.Specification
import spray.http._
import spray.http.HttpMethods._
import twitter4s.entities.{AccessToken, ConsumerToken}
import spray.http.HttpHeaders._
import spray.http.MediaTypes._


class OAuthProviderSpec extends Specification {

  implicit val consumerToken = ConsumerToken("xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")
  implicit val accessToken = AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")

  val provider = new OAuthProvider(consumerToken, accessToken) {
    override def currentMillis = 1318622958
  }

  "OAuth Provider" should {

    val uri = Uri("https://api.twitter.com/1/statuses/update.json?include_entities=true")
    val headers = List(`Content-Type`(`application/x-www-form-urlencoded`))
    val entity = HttpEntity("status=Hello%20Ladies%20%2b%20Gentlemen%2c%20a%20signed%20OAuth%20request%21")
    val request = HttpRequest(POST, uri, headers, entity)

    "provide the oauth parameters as expected" in {

      val oauthParams = provider.oauthParamsFIXME(request)
      oauthParams.size === 7
      oauthParams("oauth_consumer_key") === consumerToken.key
      oauthParams("oauth_signature_method") === "HMAC-SHA1"
      oauthParams("oauth_version") === "1.0"
      oauthParams("oauth_token") === accessToken.key
      oauthParams("oauth_nonce").size === 42
      oauthParams("oauth_timestamp") === "1318622958"
      oauthParams("oauth_signature") === "tnnArxj06cWHq44gCs1OSKk/jLY="
    }

    "generate the signature base string as expected" in {
      val expectedSignatureBase = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
      provider.signatureBase(request) === expectedSignatureBase
    }

    "generate the signing key as expected" in {
      val expectedSigningKey = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"
      provider.signingKey === expectedSigningKey
    }
  }

}
