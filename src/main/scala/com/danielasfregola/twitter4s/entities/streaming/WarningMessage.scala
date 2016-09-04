package com.danielasfregola.twitter4s.entities.streaming

/** When connected to a stream using the stall_warnings parameter,
  * you may receive status notices indicating the current health of the connection.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#stall_warnings" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#stall_warnings</a>.
  */
case class WarningMessage(warning: WarningMessageInfo) extends StreamingMessage

case class WarningMessageInfo(code: String,
                              message: String,
                              percent_full: Option[Int] = None,
                              user_id: Option[Long] = None)
