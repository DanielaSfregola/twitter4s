package twitter4s.statuses

import twitter4s.http.marshalling.Parameters

case class MentionsParameters(count: Int,
                              since_id: Option[Long],
                              max_id: Option[Long],
                              trim_user: Boolean,
                              contributor_details: Boolean,
                              include_entities: Boolean) extends Parameters

case class UserTimelineParameters(user_id: Option[Long],
                                  screen_name: Option[String],
                                  since_id: Option[Long],
                                  count: Int,
                                  max_id: Option[Long],
                                  trim_user: Boolean,
                                  exclude_replies: Boolean,
                                  contributor_details: Boolean,
                                  include_rts: Boolean) extends Parameters

case class HomeTimelineParameters(count: Int,
                                  since_id: Option[Long],
                                  max_id: Option[Long],
                                  trim_user: Boolean,
                                  exclude_replies: Boolean,
                                  contributor_details: Boolean,
                                  include_entities: Boolean) extends Parameters

case class RetweetsOfMeParameters(count: Int,
                                  since_id: Option[Long],
                                  max_id: Option[Long],
                                  trim_user: Boolean,
                                  exclude_replies: Boolean,
                                  contributor_details: Boolean,
                                  include_entities: Boolean) extends Parameters
