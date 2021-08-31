package com.danielasfregola.twitter4s.entities.v2.enums.fields

object UserFields extends Enumeration {
  type UserFields = Value

  val CreatedAt = Value("created_at")
  val Description = Value("description")
  val Entities = Value("entities")
  val Id = Value("id")
  val Location = Value("location")
  val Name = Value("name")
  val PinnedTweetId = Value("pinned_tweet_id")
  val ProfileImageUrl = Value("profile_image_url")
  val Protected = Value("protected")
  val PublicMetrics = Value("public_metrics")
  val Url = Value("url")
  val Username = Value("username")
  val Verified = Value("verified")
  val Withheld = Value("withheld")
}
