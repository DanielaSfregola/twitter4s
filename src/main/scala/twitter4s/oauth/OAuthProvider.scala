package twitter4s.oauth

import scala.util.Random

import spray.http.HttpRequest
import twitter4s.entities.{AccessToken, ConsumerToken}

class OAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken) extends HmacSha1Encoder {

  def oauthParamsFIXME(request: HttpRequest) = oauthParams + oauthSignature(request)

  protected def oauthSignature(implicit request: HttpRequest) =
    ("oauth_signature" -> toHmacSha1(signatureBase, signingKey))


  private def oauthParams: Map[String, String] = {
    val consumerKey = ("oauth_consumer_key" -> consumerToken.key)
    val signatureMethod = ("oauth_signature_method" -> "HMAC-SHA1")
    val version = ("oauth_version" -> "1.0")
    val token = ("oauth_token" -> accessToken.key)

    def nonce = ("oauth_nonce" -> Random.alphanumeric.take(42).mkString)
    def timestamp = ("oauth_timestamp" -> currentMillis.toString)

    Map(consumerKey, nonce, signatureMethod, timestamp, token, version)
  }

  protected def currentMillis = System.currentTimeMillis

  def signingKey = {
    val encodedConsumerSecret = consumerToken.secret.toAscii
    val encodedAccessTokenSecret = accessToken.secret.toAscii
    s"$encodedConsumerSecret&$encodedAccessTokenSecret"
  }

  def signatureBase(implicit request: HttpRequest) = {
    val method = request.method.toString.toAscii
    val baseUrl = {
      val uri = request.uri
      s"${uri.scheme}${uri.authority}${uri.path}"
    }.toAscii
    val params = s"$encodedBody$encodedQueryParams$encodedOAuthParams"
    s"$method&$baseUrl&$params"
  }

  private def encodedBody(implicit request: HttpRequest) = request.entity.asString.toAscii

  private def encodedQueryParams(implicit request: HttpRequest) = encodeParams(request.uri.query.toMap)

  private def encodedOAuthParams(implicit request: HttpRequest) = encodeParams(oauthParams)

  private def encodeParams(params: Map[String, String]) = {
    val encodedParams = params.map { case (k, v) => (k.toAscii, v.toAscii) }
    encodedParams.keySet.toList.sorted.map { encodedKey =>
      val encodedValue = encodedParams(encodedKey)
      s"$encodedKey=$encodedValue"
    }.mkString("&").toAscii
  }


}



