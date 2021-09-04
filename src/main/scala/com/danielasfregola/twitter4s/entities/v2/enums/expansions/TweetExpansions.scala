package com.danielasfregola.twitter4s.entities.v2.enums.expansions

object TweetExpansions extends Enumeration {
  type TweetExpansions = Value

  val `Attachments.PollIds` = Value("attachments.poll_ids")
  val `Attachments.MediaKeys` = Value("attachments.media_keys")
  val AuthorId = Value("author_id")
  val `Entities.Mentions.Username` = Value("entities.mentions.username")
  val `Geo.PlaceId` = Value("geo.place_id")
  val InReplyToUser = Value("in_reply_to_user_id")
  val `ReferencedTweets.Id` = Value("referenced_tweets.id")
  val `ReferencedTweets.Id.AuthorId` = Value("referenced_tweets.id.author_id")
}
