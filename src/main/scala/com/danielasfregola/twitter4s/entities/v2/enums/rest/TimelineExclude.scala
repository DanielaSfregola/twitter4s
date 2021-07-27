package com.danielasfregola.twitter4s.entities.v2.enums.rest

object TimelineExclude extends Enumeration {
  type TimelineExclude = Value

  val Retweets = Value("retweets")
  val Replies = Value("replies")
}
