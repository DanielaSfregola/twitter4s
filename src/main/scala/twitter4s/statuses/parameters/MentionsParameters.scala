package twitter4s.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class MentionsParameters(count: Int,
                              since_id: Option[Long],
                              max_id: Option[Long],
                              trim_user: Boolean,
                              contributor_details: Boolean,
                              include_entities: Boolean) extends Parameters
