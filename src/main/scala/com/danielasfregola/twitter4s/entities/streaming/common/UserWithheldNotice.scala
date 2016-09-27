package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** These events contain an id field indicating the user ID and a collection of
  * withheld_in_countries uppercase two-letter country codes.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices</a>.
  */
case class UserWithheldNotice(user_withheld: UserWithheldId) extends CommonStreamingMessage

case class UserWithheldId(id: Long, withheld_in_countries: List[String])
