package com.danielasfregola.twitter4s.entities.streaming

case class StatusDeletionNotice(delete: StatusDeletionNoticeInfo) extends StreamingMessage

case class StatusDeletionNoticeInfo(status: StatusDeletionId, timestamp_ms: String)

case class StatusDeletionId(id: Long,
                            id_str: String,
                            user_id: Long,
                            user_id_str: String)
