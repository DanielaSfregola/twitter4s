package com.danielasfregola.twitter4s.http.oauth

import akka.http.scaladsl.model.HttpRequest
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.{ExecutionContext, Future}

class MediaOAuthProvider(consumerToken: ConsumerToken, accessToken: AccessToken)
                        (implicit ec: ExecutionContext, m: Materializer) extends OAuthProvider(consumerToken, accessToken) {

  override def bodyParams(implicit request: HttpRequest): Future[Map[String, String]] = Future.successful(Map.empty)

}
