package twitter4s.http
package oauth

import spray.http.{HttpHeader, HttpHeaders, HttpRequest}
import twitter4s.entities.{AccessToken, ConsumerToken}

class OAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken) extends HmacSha1Encoder {

  def oauthHeader(implicit request: HttpRequest): HttpHeader = {
    val authorizationValue = oauthParams.map{ case (k, v) => s"""$k="$v""""}.toList.sorted.mkString(", ")
    HttpHeaders.RawHeader("Authorization", s"OAuth $authorizationValue")
  }

  def oauthParams(implicit request: HttpRequest): Map[String, String] = {
    val oauthParams = basicOAuthParams
    oauthParams + oauthSignature(oauthParams)(request)
  }

  def oauthSignature(oauthParams: Map[String, String])(implicit request: HttpRequest) = {
    val signatureBase = getSignatureBase(oauthParams)
    ("oauth_signature" -> toHmacSha1(signatureBase, signingKey).toAscii)
  }

  val signingKey = {
    val encodedConsumerSecret = consumerToken.secret.toAscii
    val encodedAccessTokenSecret = accessToken.secret.toAscii
    s"$encodedConsumerSecret&$encodedAccessTokenSecret"
  }

  protected def currentSecondsFromEpoc = 1442927981//System.currentTimeMillis / 1000
  protected def generateNonce = "79e37270e50fb6c21a5cb72abfe56c26"//Random.alphanumeric.take(42).mkString

  private def basicOAuthParams: Map[String, String] = {
    val consumerKey = ("oauth_consumer_key" -> consumerToken.key)
    val signatureMethod = ("oauth_signature_method" -> "HMAC-SHA1")
    val version = ("oauth_version" -> "1.0")
    val token = ("oauth_token" -> accessToken.key)
    def nonce = ("oauth_nonce" -> generateNonce)
    def timestamp = ("oauth_timestamp" -> currentSecondsFromEpoc.toString)

    Map(consumerKey, nonce, signatureMethod, timestamp, token, version)
  }

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
    val entities = request.entity.asString.split("&")
    val bodyTokens = entities.map {_.split("=", 2)}.flatten.toList
    bodyTokens.grouped(2).map { case List(k, v) => k -> v}.toMap
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



