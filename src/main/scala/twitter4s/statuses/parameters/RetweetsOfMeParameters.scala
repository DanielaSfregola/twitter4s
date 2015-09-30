package twitter4s.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class RetweetsOfMeParameters(count: Int,
                                  since_id: Option[Long],
                                  max_id: Option[Long],
                                  trim_user: Boolean,
                                  exclude_replies: Boolean,
                                  contributor_details: Boolean,
                                  include_entities: Boolean) extends Parameters
