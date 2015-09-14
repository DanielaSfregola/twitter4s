package twitter4s.oauth

import scala.util.Random

import spray.http.HttpRequest
import twitter4s.entities.{AccessToken, ConsumerToken}

class OAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken) extends HmacSha1Encoder {

  val signingKey = {
    val encodedConsumerSecret = consumerToken.secret.toAscii
    val encodedAccessTokenSecret = accessToken.secret.toAscii
    s"$encodedConsumerSecret&$encodedAccessTokenSecret"
  }

  def getOAuthParams(request: HttpRequest): Map[String, String] = {
    val oauthParams = basicOAuthParams
    oauthParams + oauthSignature(oauthParams)(request)
  }

  protected def oauthSignature(oauthParams: Map[String, String])(implicit request: HttpRequest) = {
    val signatureBase = getSignatureBase(oauthParams)
    ("oauth_signature" -> toHmacSha1(signatureBase, signingKey))
  }


  private def basicOAuthParams: Map[String, String] = {
    val consumerKey = ("oauth_consumer_key" -> consumerToken.key)
    val signatureMethod = ("oauth_signature_method" -> "HMAC-SHA1")
    val version = ("oauth_version" -> "1.0")
    val token = ("oauth_token" -> accessToken.key)
    def nonce = ("oauth_nonce" -> generateNonce)
    def timestamp = ("oauth_timestamp" -> currentMillis.toString)

    Map(consumerKey, nonce, signatureMethod, timestamp, token, version)
  }

  protected def currentMillis = System.currentTimeMillis
  protected def generateNonce = Random.alphanumeric.take(42).mkString

  def getSignatureBase(oauthParams: Map[String, String])(implicit request: HttpRequest) = {
    val method = request.method.toString.toAscii
    val baseUrl = {
      val uri = request.uri
      s"${uri.scheme}:${uri.authority}${uri.path}"
    }.toAscii
    val encodedParams = encodeParams(queryParams ++ oauthParams ++ bodyParams)
    s"$method&$baseUrl&$encodedParams"
  }

  private def bodyParams(implicit request: HttpRequest): Map[String, String] = {
    val body = request.entity.asString
    body.split("=", 2).toList.grouped(2).map { case List(k,v) => k -> v }.toMap
  }

  private def queryParams(implicit request: HttpRequest) = request.uri.query.toMap

  private def encodeParams(params: Map[String, String]) = {
    val encodedParams = params.map { case (k, v) => (k.toAscii, v.toAscii) }
    encodedParams.keySet.toList.sorted.map { encodedKey =>
      val encodedValue = encodedParams(encodedKey)
      s"$encodedKey=$encodedValue"
    }.mkString("&").toAscii
  }


}



