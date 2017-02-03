package com.danielasfregola.twitter4s.entities

case class RateLimitData[T](rate_limit: RateLimit, data: T)

