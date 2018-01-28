package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** These messages indicate that a filtered stream has matched more Tweets
  * than its current rate limit allows to be delivered. Limit notices contain
  * a total count of the number of undelivered Tweets since the connection was opened,
  * making them useful for tracking counts of track terms, for example.
  * Note that the counts do not specify which filter predicates undelivered messages matched.
  * For more information see
  * <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>.
  */
final case class LimitNotice(limit: LimitTrack) extends CommonStreamingMessage

final case class LimitTrack(track: Long)
