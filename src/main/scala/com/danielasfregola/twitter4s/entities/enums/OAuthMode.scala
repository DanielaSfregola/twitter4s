package com.danielasfregola.twitter4s.entities.enums

object OAuthMode extends Enumeration {
  type OAuthMode = Value

  val UseOAuth1 = Value(1)
  val UseOAuthBearerToken = Value(2)
  val MixedAuth = Value(3)
}
