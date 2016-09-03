package com.danielasfregola.twitter4s.entities.streaming

case class UserWithheldNotice(status_withheld: UserWithheldId) extends StreamingMessage

case class UserWithheldId(id: Long,
                          id_str: String,
                          user_id: Long,
                          user_id_str: String,
                          withheld_in_countries: List[String])
