package twitter4s.statuses

import twitter4s.http.marshalling.Options

case class MentionsOptions(count: Option[Int] = None,
                           since_id: Option[Long] = None,
                           max_id: Option[Long] = None,
                           trim_user: Boolean = false,
                           contributor_details: Boolean = false,
                           include_entities: Boolean = true) extends Options
