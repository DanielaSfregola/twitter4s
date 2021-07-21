package com.danielasfregola.twitter4s.entities.v2.enums.expansions

object TweetExpansions extends Enumeration {
  type Expansions = Value

  // val `Attachments.PollIds` = Value("attachments.poll_ids") // TODO: Pending addition of poll model
  // val `Attachments.MediaKeys` = Value("attachments.media_keys") // TODO: Pending addition of media model
  // val AuthorId = Value("author_id") // TODO: Pending addition of user model
  val `Entities.Mentions.Username` = Value("entities.mentions.username")
  //  val `Geo.PlaceId` = Value("geo.place_id") // TODO: Pending addition of place model
  val InReplyToUser = Value("in_reply_to_user_id")
  val `ReferencedTweets.Id` = Value("referenced_tweets.id")
  val `ReferencedTweets.Id.AuthorId` = Value("referenced_tweets.id.author_id")
}
