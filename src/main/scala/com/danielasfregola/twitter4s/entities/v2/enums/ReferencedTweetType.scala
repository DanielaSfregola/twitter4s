package com.danielasfregola.twitter4s.entities.v2.enums

object ReferencedTweetType extends Enumeration {
  type ReferencedTweetType = Value

  val Retweeted = Value("retweeted")
  val Quoted = Value("quoted")
  val RepliedTo = Value("replied_to")
}
