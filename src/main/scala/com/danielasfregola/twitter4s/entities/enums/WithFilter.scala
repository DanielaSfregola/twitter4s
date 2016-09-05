package com.danielasfregola.twitter4s.entities.enums

object WithFilter extends Enumeration {
  type WithFilter = Value

  val User = Value("user")
  val Followings = Value("followings")
}
