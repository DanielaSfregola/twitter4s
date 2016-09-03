package com.danielasfregola.twitter4s.entities.streaming

case class StatusWithheldNotice(status_withheld: StatusWithheldId) extends StreamingMessage

case class StatusWithheldId(id: Long,
                            id_str: String,
                            user_id: Long,
                            user_id_str: String,
                            withheld_in_countries: List[String])

