package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** These messages indicate that a given Tweet has been deleted.
  * OldClient code must honor these messages by clearing the referenced Tweet from memory and any
  * storage or archive, even in the rare case where a deletion message arrives earlier
  * in the stream that the Tweet it references.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#status_deletion_notices_delete" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#status_deletion_notices_delete</a>.
  */
case class StatusDeletionNotice(delete: StatusDeletionNoticeInfo) extends CommonStreamingMessage

case class StatusDeletionNoticeInfo(status: StatusDeletionId)

case class StatusDeletionId(id: Long, id_str: String, user_id: Long, user_id_str: String)
