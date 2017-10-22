package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** These events contain an id field indicating the user ID and a collection of
  * withheld_in_countries uppercase two-letter country codes.
  * For more information see
  * <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>.
  */
final case class UserWithheldNotice(user_withheld: UserWithheldId) extends CommonStreamingMessage

final case class UserWithheldId(id: Long, withheld_in_countries: List[String])
