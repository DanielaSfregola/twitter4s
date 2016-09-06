package com.danielasfregola.twitter4s.entities.streaming

/** Site Streams are sent the same messages as User Streams (including friends lists in the preamble),
  * but for multiple users instead of a single user.The same types of messages are streamed, but to identify the target of each message,
  * an additional wrapper is placed around every message, except for blank keep-alive lines.
  * For more information see <a href="https://dev.twitter.com/streaming/overview/messages-types#envelopes_for_user" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#envelopes_for_user</a>
  */
case class UserEnvelop(for_user: Long, message: StreamingMessage) extends StreamingMessage

/** Site Streams are sent the same messages as User Streams (including friends lists in the preamble),
  * but for multiple users instead of a single user.The same types of messages are streamed, but to identify the target of each message,
  * an additional wrapper is placed around every message, except for blank keep-alive lines.
  * For more information see <a href="https://dev.twitter.com/streaming/overview/messages-types#envelopes_for_user" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#envelopes_for_user</a>
  */
case class UserEnvelopStringified(for_user: String, message: StreamingMessage) extends StreamingMessage
