package com.danielasfregola.twitter4s.http
package oauth

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpCharsets, _}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.helpers.{AwaitableFuture, TestActorSystem, TestExecutionContext}
import org.specs2.matcher.Scope
import org.specs2.mutable.SpecificationLike

class OAuth1ProviderSpec extends TestActorSystem with SpecificationLike with AwaitableFuture with TestExecutionContext {

  val consumerToken = ConsumerToken("xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")

  val request = {
    val uri = Uri("https://api.twitter.com/1/statuses/update.json?include_entities=true")
    val contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`)
    val entity = HttpEntity("status=Hello+Ladies+%2B+Gentlemen%2C+a+signed+OAuth+request%21")
    HttpRequest(method = HttpMethods.POST, uri = uri, entity = entity.withContentType(contentType))
  }

  def providerBuilder(accessToken: Option[AccessToken]) = new OAuth1Provider(consumerToken, accessToken) {
    override def currentSecondsFromEpoc = 1318622958
    override def generateNonce = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"
  }

  abstract class OAuthWithAccessToken extends Scope {
    implicit val accessToken =
      AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")
    val provider = providerBuilder(Some(accessToken))
  }

  abstract class OAuthWithNoAccessToken extends Scope {
    val provider = providerBuilder(None)
  }

  trait WithCallback {
    val callback = Some("http://my.example/auth")
  }

  "OAuth Provider" should {

    "with no callback" in {

      "with access token" in {

        "provide an Authorization token according to the OAuth standards" in new OAuthWithAccessToken {
          val oauthHeader = provider.oauth1Header(None)(request, materializer).await
          val expectedAuthorization =
            """OAuth oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog", oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg", oauth_signature="tnnArxj06cWHq44gCs1OSKk%2FjLY%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1318622958", oauth_token="370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", oauth_version="1.0""""
          oauthHeader === RawHeader("Authorization", expectedAuthorization)
        }

        "provide the oauth parameters as expected" in new OAuthWithAccessToken {

          val oauthParams = provider.oauth1Params(None)(request, materializer).await
          oauthParams.size === 7
          oauthParams("oauth_consumer_key") === consumerToken.key
          oauthParams("oauth_signature_method") === "HMAC-SHA1"
          oauthParams("oauth_version") === "1.0"
          oauthParams("oauth_token") === accessToken.key
          oauthParams("oauth_nonce").size === 42
          oauthParams("oauth_timestamp") === "1318622958"
          oauthParams("oauth_signature") === "tnnArxj06cWHq44gCs1OSKk%2FjLY%3D"
        }

        "generate the signature base string as expected" in new OAuthWithAccessToken {
          val oauthParams = Map(
            "oauth_consumer_key" -> consumerToken.key,
            "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
            "oauth_signature_method" -> "HMAC-SHA1",
            "oauth_timestamp" -> "1318622958",
            "oauth_token" -> accessToken.key,
            "oauth_version" -> "1.0"
          )
          val expectedSignatureBase =
            "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
          provider.signatureBase(oauthParams)(request, materializer).await === expectedSignatureBase
        }

        "generate the signing key as expected" in new OAuthWithAccessToken {
          val expectedSigningKey =
            "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"
          provider.signingKey === expectedSigningKey
        }

        "extract body parameters from a request with body as expected" in new OAuthWithAccessToken {
          val bodyParams = provider.bodyParams(request, materializer).await
          bodyParams === Map("status" -> "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21")
        }

        "extract body parameters from a request without body as expected" in new OAuthWithAccessToken {
          val requestWithoutBody = request.withEntity(HttpEntity.Empty)
          val bodyParams = provider.bodyParams(requestWithoutBody, materializer).await
          bodyParams === Map()
        }
      }

      "with no access token" in {

        "provide an Authorization token according to the OAuth standards" in new OAuthWithNoAccessToken {
          val oauthHeader = provider.oauth1Header(None)(request, materializer).await
          val expectedAuthorization =
            """OAuth oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog", oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg", oauth_signature="%2Bgxx4CGoDB7afZbRRRpR56orbKU%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1318622958", oauth_version="1.0""""
          oauthHeader === RawHeader("Authorization", expectedAuthorization)
        }

        "provide the oauth parameters as expected" in new OAuthWithNoAccessToken {

          val oauthParams = provider.oauth1Params(None)(request, materializer).await
          oauthParams.size === 6
          oauthParams("oauth_consumer_key") === consumerToken.key
          oauthParams("oauth_signature_method") === "HMAC-SHA1"
          oauthParams("oauth_version") === "1.0"
          oauthParams("oauth_nonce").size === 42
          oauthParams("oauth_timestamp") === "1318622958"
          oauthParams("oauth_signature") === "%2Bgxx4CGoDB7afZbRRRpR56orbKU%3D"
        }

        "generate the signature base string as expected" in new OAuthWithNoAccessToken {
          val oauthParams = Map(
            "oauth_consumer_key" -> consumerToken.key,
            "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
            "oauth_signature_method" -> "HMAC-SHA1",
            "oauth_timestamp" -> "1318622958",
            "oauth_version" -> "1.0"
          )
          val expectedSignatureBase =
            "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
          provider.signatureBase(oauthParams)(request, materializer).await === expectedSignatureBase
        }

        "generate the signing key as expected" in new OAuthWithNoAccessToken {
          val expectedSigningKey = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&"
          provider.signingKey === expectedSigningKey
        }

        "extract body parameters from a request with body as expected" in new OAuthWithNoAccessToken {
          val bodyParams = provider.bodyParams(request, materializer).await
          bodyParams === Map("status" -> "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21")
        }

        "extract body parameters from a request without body as expected" in new OAuthWithNoAccessToken {
          val requestWithoutBody = request.withEntity(HttpEntity.Empty)
          val bodyParams = provider.bodyParams(requestWithoutBody, materializer).await
          bodyParams === Map()
        }
      }
    }

    "with callback" in {

      "with access token" in {

        "provide an Authorization token according to the OAuth standards" in new OAuthWithAccessToken
        with WithCallback {
          val oauthHeader = provider.oauth1Header(callback)(request, materializer).await
          val expectedAuthorization =
            """OAuth oauth_callback="http%3A%2F%2Fmy.example%2Fauth", oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog", oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg", oauth_signature="qiHvwU1c%2Bi7rCRk8rnVGOHXXTBI%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1318622958", oauth_token="370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb", oauth_version="1.0""""
          oauthHeader === RawHeader("Authorization", expectedAuthorization)
        }

        "provide the oauth parameters as expected" in new OAuthWithAccessToken with WithCallback {

          val oauthParams = provider.oauth1Params(callback)(request, materializer).await
          oauthParams.size === 8
          oauthParams("oauth_callback") === "http%3A%2F%2Fmy.example%2Fauth"
          oauthParams("oauth_consumer_key") === consumerToken.key
          oauthParams("oauth_signature_method") === "HMAC-SHA1"
          oauthParams("oauth_version") === "1.0"
          oauthParams("oauth_token") === accessToken.key
          oauthParams("oauth_nonce").size === 42
          oauthParams("oauth_timestamp") === "1318622958"
          oauthParams("oauth_signature") === "qiHvwU1c%2Bi7rCRk8rnVGOHXXTBI%3D"
        }

        "generate the signature base string as expected" in new OAuthWithAccessToken with WithCallback {
          val oauthParams = Map(
            "oauth_callback" -> "http%3A%2F%2Fmy.example%2Fauth",
            "oauth_consumer_key" -> consumerToken.key,
            "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
            "oauth_signature_method" -> "HMAC-SHA1",
            "oauth_timestamp" -> "1318622958",
            "oauth_token" -> accessToken.key,
            "oauth_version" -> "1.0"
          )
          val expectedSignatureBase =
            "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_callback%3Dhttp%25253A%25252F%25252Fmy.example%25252Fauth%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
          provider.signatureBase(oauthParams)(request, materializer).await === expectedSignatureBase
        }

        "generate the signing key as expected" in new OAuthWithAccessToken with WithCallback {
          val expectedSigningKey =
            "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"
          provider.signingKey === expectedSigningKey
        }

        "extract body parameters from a request with body as expected" in new OAuthWithAccessToken with WithCallback {
          val bodyParams = provider.bodyParams(request, materializer).await
          bodyParams === Map("status" -> "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21")
        }

        "extract body parameters from a request without body as expected" in new OAuthWithAccessToken
        with WithCallback {
          val requestWithoutBody = request.withEntity(HttpEntity.Empty)
          val bodyParams = provider.bodyParams(requestWithoutBody, materializer).await
          bodyParams === Map()
        }
      }

      "with no access token" in {

        "provide an Authorization token according to the OAuth standards" in new OAuthWithNoAccessToken
        with WithCallback {
          val oauthHeader = provider.oauth1Header(callback)(request, materializer).await
          val expectedAuthorization =
            """OAuth oauth_callback="http%3A%2F%2Fmy.example%2Fauth", oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog", oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg", oauth_signature="Nb7%2FehS1ddvtYCbI9VU1AJyBKS8%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1318622958", oauth_version="1.0""""
          oauthHeader === RawHeader("Authorization", expectedAuthorization)
        }

        "provide the oauth parameters as expected" in new OAuthWithNoAccessToken with WithCallback {

          val oauthParams = provider.oauth1Params(callback)(request, materializer).await
          oauthParams.size === 7
          oauthParams("oauth_callback") === "http%3A%2F%2Fmy.example%2Fauth"
          oauthParams("oauth_consumer_key") === consumerToken.key
          oauthParams("oauth_signature_method") === "HMAC-SHA1"
          oauthParams("oauth_version") === "1.0"
          oauthParams("oauth_nonce").size === 42
          oauthParams("oauth_timestamp") === "1318622958"
          oauthParams("oauth_signature") === "Nb7%2FehS1ddvtYCbI9VU1AJyBKS8%3D"
        }

        "generate the signature base string as expected" in new OAuthWithNoAccessToken with WithCallback {
          val oauthParams = Map(
            "oauth_callback" -> "http%3A%2F%2Fmy.example%2Fauth",
            "oauth_consumer_key" -> consumerToken.key,
            "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
            "oauth_signature_method" -> "HMAC-SHA1",
            "oauth_timestamp" -> "1318622958",
            "oauth_version" -> "1.0"
          )
          val expectedSignatureBase =
            "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_callback%3Dhttp%25253A%25252F%25252Fmy.example%25252Fauth%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
          provider.signatureBase(oauthParams)(request, materializer).await === expectedSignatureBase
        }

        "generate the signing key as expected" in new OAuthWithNoAccessToken with WithCallback {
          val expectedSigningKey = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&"
          provider.signingKey === expectedSigningKey
        }

        "extract body parameters from a request with body as expected" in new OAuthWithNoAccessToken with WithCallback {
          val bodyParams = provider.bodyParams(request, materializer).await
          bodyParams === Map("status" -> "Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21")
        }

        "extract body parameters from a request without body as expected" in new OAuthWithNoAccessToken
        with WithCallback {
          val requestWithoutBody = request.withEntity(HttpEntity.Empty)
          val bodyParams = provider.bodyParams(requestWithoutBody, materializer).await
          bodyParams === Map()
        }
      }
    }

  }

}
