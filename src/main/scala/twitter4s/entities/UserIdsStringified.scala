package twitter4s.entities

case class UserIdsStringified(ids: Seq[String] = Seq.empty, next_cursor: Long, previous_cursor: Long)
