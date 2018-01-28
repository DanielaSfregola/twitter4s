package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** When connected to a stream using the stall_warnings parameter,
  * you may receive status notices indicating the current health of the connection.
  * For more information see
  * <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>.
  */
final case class WarningMessage(warning: WarningMessageInfo) extends CommonStreamingMessage

final case class WarningMessageInfo(code: String,
                                    message: String,
                                    percent_full: Option[Int] = None,
                                    user_id: Option[Long] = None)
