package com.danielasfregola.twitter4s.entities.streaming

/** These messages indicate that a filtered stream has matched more Tweets
  * than its current rate limit allows to be delivered. Limit notices contain
  * a total count of the number of undelivered Tweets since the connection was opened,
  * making them useful for tracking counts of track terms, for example.
  * Note that the counts do not specify which filter predicates undelivered messages matched.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#limit_notices" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#limit_notices</a>.
  */
case class LimitNotice(limit: LimitTrack) extends StreamingMessage

case class LimitTrack(track: Long)


