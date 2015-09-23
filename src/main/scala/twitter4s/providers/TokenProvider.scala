package twitter4s.providers

import twitter4s.entities.{AccessToken, ConsumerToken}

trait TokenProvider {
  val consumerToken: ConsumerToken
  val accessToken: AccessToken
}
