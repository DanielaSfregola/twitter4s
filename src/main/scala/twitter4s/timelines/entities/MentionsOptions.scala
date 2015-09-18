package twitter4s.timelines.entities

case class MentionsOptions(count: Option[Int] = None,
                           sinceId: Option[Long] = None,
                           maxId: Option[Long] = None,
                           trimUser: Boolean = false,
                           contributorDetails: Boolean = false,
                           includeEntities: Boolean = true)
