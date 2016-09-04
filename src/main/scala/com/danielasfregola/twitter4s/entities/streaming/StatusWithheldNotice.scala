package com.danielasfregola.twitter4s.entities.streaming

/** These events contain an id field indicating the status ID, a user_id indicating the user,
  * and a collection of withheld_in_countries uppercase two-letter country codes.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#withheld_content_notices</a>.
  */
case class StatusWithheldNotice(status_withheld: StatusWithheldId) extends StreamingMessage

case class StatusWithheldId(id: Long,
                            user_id: Long,
                            withheld_in_countries: List[String])

