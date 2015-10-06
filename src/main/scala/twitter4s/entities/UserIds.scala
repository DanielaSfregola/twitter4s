package twitter4s.entities

case class UserIds(ids: Seq[Long] = Seq.empty, next_cursor: Int, previous_cursor: Int)
