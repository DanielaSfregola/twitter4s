package com.danielasfregola.twitter4s.helpers

import akka.http.scaladsl.model.{HttpCharsets, MediaTypes}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.UriHelpers
import org.specs2.matcher.Scope
import org.specs2.mutable.SpecificationLike

trait Spec extends SpecificationLike with UriHelpers {

  val `application/x-www-form-urlencoded` = MediaTypes.`application/x-www-form-urlencoded`
  val `text/plain` = MediaTypes.`text/plain` withCharset HttpCharsets.`UTF-8`

  trait SpecContext extends FixturesSupport with AwaitableFuture with Scope {

    val consumerToken = ConsumerToken("consumer-key", "consumer-secret")
    val accessToken = AccessToken("access-key", "access-secret")

  }
}
