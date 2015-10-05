package twitter4s.entities

case class RetweetersIds(ids: Seq[Long] = Seq.empty, next_cursor: Int, previous_cursor: Int)
