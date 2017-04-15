package com.danielasfregola.twitter4s.http
package oauth

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.{Encoder, UriHelpers}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Random

private[twitter4s] class OAuth2Provider(consumerToken: ConsumerToken, accessToken: AccessToken) extends Encoder with UriHelpers {

  def oauth2Header(implicit request: HttpRequest, materializer: Materializer): Future[HttpHeader] = {
    implicit val ec = materializer.executionContext
    oauth2Params.map { params =>
      val authorizationValue = params.map { case (k, v) => s"""$k="$v"""" }.toList.sorted.mkString(", ")
      RawHeader("Authorization", s"OAuth $authorizationValue")
    }
  }

  def oauth2Params(implicit request: HttpRequest, materializer: Materializer): Future[Map[String, String]] = {
    implicit val ec = materializer.executionContext
    val params = basicOAuth2Params
    for {
      signature <- oauth2Signature(params)
    } yield (params + signature).mapValues(_.toAscii)
  }

  def oauth2Signature(oauth2Params: Map[String, String])(implicit request: HttpRequest, materializer: Materializer) = {
    implicit val ec = materializer.executionContext
    signatureBase(oauth2Params).map { signatureBase =>
      "oauth_signature" -> toHmacSha1(signatureBase, signingKey)
    }
  }

  val signingKey = {
    val encodedConsumerSecret = consumerToken.secret.toAscii
    val encodedAccessTokenSecret = accessToken.secret.toAscii
    s"$encodedConsumerSecret&$encodedAccessTokenSecret"
  }

  protected def currentSecondsFromEpoc = System.currentTimeMillis / 1000
  protected def generateNonce = Random.alphanumeric.take(42).mkString

  private def basicOAuth2Params: Map[String, String] = {
    val consumerKey = ("oauth_consumer_key" -> consumerToken.key)
    val signatureMethod = ("oauth_signature_method" -> "HMAC-SHA1")
    val version = ("oauth_version" -> "1.0")
    val token = ("oauth_token" -> accessToken.key)
    def nonce = ("oauth_nonce" -> generateNonce)
    def timestamp = ("oauth_timestamp" -> currentSecondsFromEpoc.toString)

    Map(consumerKey, nonce, signatureMethod, timestamp, token, version)
  }

  def signatureBase(oauth2Params: Map[String, String])(implicit request: HttpRequest, materializer: Materializer) = {
    implicit val ec = materializer.executionContext
    bodyParams.map { bdParams =>
      val method = request.method.name.toAscii
      val baseUrl = request.uri.endpoint.toAscii
      val encodedParams = encodeParams(queryParams ++ oauth2Params ++ bdParams).toAscii
      s"$method&$baseUrl&$encodedParams"
    }
  }

  def bodyParams(implicit request: HttpRequest, materializer: Materializer): Future[Map[String, String]] = {
    implicit val ec = materializer.executionContext
    extractRequestBody.map { body =>
      val cleanBody = body.replace("+", "%20")
      if (cleanBody.nonEmpty) {
        val entities = cleanBody.split("&")
        val bodyTokens = entities.flatMap {
          _.split("=", 2)
        }.toList
        bodyTokens.grouped(2).map { case List(k, v) => k -> v }.toMap
      } else Map()
    }
  }

  def queryParams(implicit request: HttpRequest) = request.uri.query().toMap.mapValues(_.toAscii)

  private def encodeParams(params: Map[String, String]) =
    params.keySet.toList.sorted.map { key =>
      val value = params(key)
      s"$key=$value"
    }.mkString("&")

  private def extractRequestBody(implicit request: HttpRequest, materializer: Materializer): Future[String] = {
    implicit val ec = materializer.executionContext
    request.entity.withoutSizeLimit.toStrict(5 seconds).map(_.data.utf8String)
  }



}



