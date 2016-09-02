package com.danielasfregola.twitter4s.entities.streaming

case class StatusDeletionNotice(delete: StatusDeletionId) extends StreamingMessage

case class StatusDeletionId(id: Long,
                            id_str: String,
                            user_id: Long,
                            user_id_str: String)
