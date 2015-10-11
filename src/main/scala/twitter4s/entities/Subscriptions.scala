package twitter4s.entities

case class Subscriptions(lists: Seq[Subscription] = Seq.empty, next_cursor: Long, previous_cursor: Long)
