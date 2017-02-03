package com.danielasfregola.twitter4s.entities

case class RatedData[T](rate_limit: RateLimit, data: T)

