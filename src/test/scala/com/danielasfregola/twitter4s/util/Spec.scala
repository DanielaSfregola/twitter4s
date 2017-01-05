package com.danielasfregola.twitter4s.util

import akka.http.scaladsl.model.{HttpCharsets, MediaTypes}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import org.specs2.matcher.Scope
import org.specs2.mutable.SpecificationLike

trait Spec extends SpecificationLike {

  val `application/x-www-form-urlencoded` = MediaTypes.`application/x-www-form-urlencoded` withCharset HttpCharsets.`UTF-8`

  trait SpecContext extends FixturesSupport with AwaitableFuture with Scope {

    val consumerToken = ConsumerToken("consumer-key", "consumer-secret")
    val accessToken = AccessToken("access-key", "access-secret")

  }

}
