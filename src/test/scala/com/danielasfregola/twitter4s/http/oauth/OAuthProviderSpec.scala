package com.danielasfregola.twitter4s.http.oauth

import akka.http.scaladsl.model.{HttpCharsets, _}
import org.specs2.mutable.{Specification, SpecificationLike}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.{AwaitableFuture, TestActorSystem}
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.ActorMaterializer


class OAuthProviderSpec extends TestActorSystem with SpecificationLike with AwaitableFuture {

  implicit val consumerToken = ConsumerToken("xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")
  implicit val accessToken = AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")

  val provider = new OAuthProvider(consumerToken, accessToken) {
    override def currentSecondsFromEpoc = 1318622958
    override def generateNonce = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"
  }

  "OAuth Provider" should {

    val uri = Uri("https://api.twitter.com/1/statuses/update.json?include_entities=true")
    val contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`)
    val entity = HttpEntity("status=Hello+Ladies+%2B+Gentlemen%2C+a+signed+OAuth+request%21")
    val request = HttpRequest(method = HttpMethods.POST, uri = uri, entity = entity.withContentType(contentType))

    "provide an Authorization token according to the OAuth standards" in {
      val oauthHeader = provider.oauthHeader(request).await
      val expectedAuthorization = """OAuth oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog", oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg", oauth_signature="tnnArxj06cWHq44gCs1OSKk%2FjLY%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1318622958", oauth_token="370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", oauth_version="1.0""""
      oauthHeader === RawHeader("Authorization", expectedAuthorization)
    }

    "provide the oauth parameters as expected" in {

      val oauthParams = provider.oauthParams(request).await
      oauthParams.size === 7
      oauthParams("oauth_consumer_key") === consumerToken.key
      oauthParams("oauth_signature_method") === "HMAC-SHA1"
      oauthParams("oauth_version") === "1.0"
      oauthParams("oauth_token") === accessToken.key
      oauthParams("oauth_nonce").size === 42
      oauthParams("oauth_timestamp") === "1318622958"
      oauthParams("oauth_signature") === "tnnArxj06cWHq44gCs1OSKk%2FjLY%3D"
    }

    "generate the signature base string as expected" in {
      val oauthParams = Map(
        "oauth_consumer_key"-> consumerToken.key,
        "oauth_signature_method" -> "HMAC-SHA1",
        "oauth_version" -> "1.0",
        "oauth_token" -> accessToken.key,
        "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
        "oauth_timestamp" -> "1318622958")
      val expectedSignatureBase = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
      provider.signatureBase(oauthParams)(request).await === expectedSignatureBase
    }

    "generate the signing key as expected" in {
      val expectedSigningKey = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"
      provider.signingKey === expectedSigningKey
    }

    "extract body parameters from a request with body as expected" in {
      val bodyParams = provider.bodyParams(request).await
      bodyParams === Map("status" -> "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21")
    }

    "extract body parameters from a request without body as expected" in {
      val requestWithoutBody = request.withEntity(HttpEntity.Empty)
      val bodyParams = provider.bodyParams(requestWithoutBody).await
      bodyParams === Map()
    }
  }

}
