package com.danielasfregola.twitter4s.entities.v2.enums.fields

object TweetFields extends Enumeration {
  type TweetFields = Value

  val Attachments = Value("attachments")
  val AuthorId = Value("author_id")
  val ContextAnnotations = Value("context_annotations")
  val ConversationId = Value("conversation_id")
  val CreatedAt = Value("created_at")
  val Entities = Value("entities")
  val Geo = Value("geo")
  val Id = Value("id")
  val InReplyToUserId = Value("in_reply_to_user_id")
  val Lang = Value("lang")
  val NonPublicMetrics = Value("non_public_metrics")
  val PublicMetrics = Value("public_metrics")
  val OrganicMetrics = Value("organic_metrics")
  val PromotedMetrics = Value("promoted_metrics")
  val PossiblySensitive = Value("possibly_sensitive")
  val ReferencedTweets = Value("referenced_tweets")
  val ReplySettings = Value("reply_settings")
  val Source = Value("source")
  val Text = Value("text")
  val Withheld = Value("withheld")
}
