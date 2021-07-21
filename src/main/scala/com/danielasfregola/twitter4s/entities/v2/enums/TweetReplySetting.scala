package com.danielasfregola.twitter4s.entities.v2.enums

object TweetReplySetting extends Enumeration {
  type TweetReplySetting = Value

  val Everyone = Value("everyone")
  val MentionedUsers = Value("mentioned_users")
  val Followers = Value("followers")
}
