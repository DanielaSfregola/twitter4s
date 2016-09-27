package com.danielasfregola.twitter4s.entities.enums

object EventCode extends Enumeration {
   type EventCode = Value

  val AccessRevoked = Value("access_revoked")
  val Block = Value("block")
  val Unblock = Value("unblock")
  val Favorite = Value("favorite")
  val FavoritedRetweet = Value("favorited_retweet")
  val Unfavorite = Value("unfavorite")
  val Follow = Value("follow")
  val Unfollow = Value("unfollow")
  val ListCreated = Value("list_created")
  val ListDestroyed = Value("list_destroyed")
  val ListUpdated = Value("list_updated")
  val ListMemberAdded = Value("list_member_added")
  val ListMemberRemoved = Value("list_member_removed")
  val ListUserSubscribed = Value("list_user_subscribed")
  val ListUserUnsubscribed = Value("list_user_unsubscribed")
  val QuotedTweet = Value("quoted_tweet")
  val UserUpdate = Value("user_update")
}
