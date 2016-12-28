package com.danielasfregola.twitter4s.http
package oauth

import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.Encoder

import scala.util.Random
import spray.http.{HttpHeader, HttpHeaders, HttpRequest}

class OldOAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken) extends Encoder {

  def oauthHeader(implicit request: HttpRequest): HttpHeader = {
    val authorizationValue = oauthParams.map{ case (k, v) => s"""$k="$v""""}.toList.sorted.mkString(", ")
    HttpHeaders.RawHeader("Authorization", s"OAuth $authorizationValue")
  }

  def oauthParams(implicit request: HttpRequest): Map[String, String] = {
    val oauthParams = basicOAuthParams
    (oauthParams + oauthSignature(oauthParams)(request)).mapValues(_.toAscii)
  }

  def oauthSignature(oauthParams: Map[String, String])(implicit request: HttpRequest) = {
    val signatureBase = getSignatureBase(oauthParams)
    ("oauth_signature" -> toHmacSha1(signatureBase, signingKey))
  }

  val signingKey = {
    val encodedConsumerSecret = consumerToken.secret.toAscii
    val encodedAccessTokenSecret = accessToken.secret.toAscii
    s"$encodedConsumerSecret&$encodedAccessTokenSecret"
  }

  protected def currentSecondsFromEpoc = System.currentTimeMillis / 1000
  protected def generateNonce = Random.alphanumeric.take(42).mkString

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
    val baseUrl = request.uri.endpoint.toAscii
    val encodedParams = encodeParams(queryParams ++ oauthParams ++ bodyParams).toAscii
    s"$method&$baseUrl&$encodedParams"
  }

  def bodyParams(implicit request: HttpRequest): Map[String, String] = {
    val body = request.entity.asString.replace("+", "%20")
    if (!body.isEmpty) {
      val entities = body.split("&")
      val bodyTokens = entities.flatMap {_.split("=", 2)}.toList
      bodyTokens.grouped(2).map { case List(k, v) => k -> v}.toMap
    } else Map()
 }

  def queryParams(implicit request: HttpRequest) = request.uri.query.toMap.mapValues(_.toAscii)

  private def encodeParams(params: Map[String, String]) =
    params.keySet.toList.sorted.map { key =>
      val value = params(key)
      s"$key=$value"
    }.mkString("&")



}



