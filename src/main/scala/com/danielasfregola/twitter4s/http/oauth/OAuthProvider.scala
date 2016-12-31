package com.danielasfregola.twitter4s.http
package oauth

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.Encoder

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class OAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken)
                   (implicit ec: ExecutionContext, m: Materializer) extends Encoder {

  def oauthHeader(implicit request: HttpRequest): Future[HttpHeader] =
    oauthParams.map { params =>
      val authorizationValue = params.map{ case (k, v) => s"""$k="$v""""}.toList.sorted.mkString(", ")
      RawHeader("Authorization", s"OAuth $authorizationValue")
    }

  def oauthParams(implicit request: HttpRequest): Future[Map[String, String]] = {
    val params = basicOAuthParams
    for {
      signature <- oauthSignature(params)(request)
    } yield (params + signature).mapValues(_.toAscii)
  }

  def oauthSignature(oauthParams: Map[String, String])(implicit request: HttpRequest) =
    signatureBase(oauthParams).map { signatureBase =>
      "oauth_signature" -> toHmacSha1(signatureBase, signingKey)
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

  def signatureBase(oauthParams: Map[String, String])(implicit request: HttpRequest) =
    bodyParams.map { bdParams =>
      val method = request.method.name.toAscii
      val baseUrl = request.uri.endpoint.toAscii
      val encodedParams = encodeParams(queryParams ++ oauthParams ++ bdParams).toAscii
      s"$method&$baseUrl&$encodedParams"
    }

  def bodyParams(implicit request: HttpRequest): Future[Map[String, String]] =
    extractRequestBody.map { body =>
      val cleanBody = body.replace("+", "%20")
      if (!cleanBody.isEmpty) {
        val entities = cleanBody.split("&")
        val bodyTokens = entities.flatMap {_.split("=", 2)}.toList
        bodyTokens.grouped(2).map { case List(k, v) => k -> v}.toMap
      } else Map()
    }

  def queryParams(implicit request: HttpRequest) = request.uri.query().toMap.mapValues(_.toAscii)

  private def encodeParams(params: Map[String, String]) =
    params.keySet.toList.sorted.map { key =>
      val value = params(key)
      s"$key=$value"
    }.mkString("&")

  private def extractRequestBody(implicit request: HttpRequest): Future[String] =
    request.entity.withoutSizeLimit.toStrict(5 seconds).map(_.data.utf8String)



}



