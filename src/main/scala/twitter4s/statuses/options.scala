package twitter4s.statuses

import twitter4s.http.marshalling.Parameters

case class MentionsParameters(
                          count: Option[Int],
                          since_id: Option[Long],
                          max_id: Option[Long],
                          trim_user: Boolean,
                          contributor_details: Boolean,
                          include_entities: Boolean
                          ) extends Parameters

case class TimelineParameters(
                          user_id: Option[Long],
                          screen_name: Option[String],
                          since_id: Option[Long],
                          count: Option[Int],
                          max_id: Option[Long],
                          trim_user: Boolean,
                          exclude_replies: Boolean,
                          contributor_details: Boolean,
                          include_rts: Boolean
                          ) extends Parameters
