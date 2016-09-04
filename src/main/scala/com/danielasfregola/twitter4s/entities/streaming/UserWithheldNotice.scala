package com.danielasfregola.twitter4s.entities.streaming

/** These events contain an id field indicating the user ID and a collection of
  * withheld_in_countries uppercase two-letter country codes.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices</a>.
  */
case class UserWithheldNotice(status_withheld: UserWithheldId) extends StreamingMessage

case class UserWithheldId(id: Long,
                          id_str: String,
                          user_id: Long,
                          user_id_str: String,
                          withheld_in_countries: List[String])
