package com.danielasfregola.twitter4s.providers

import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

trait TokenProvider {
  val consumerToken: ConsumerToken
  val accessToken: AccessToken
}
