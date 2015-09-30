package twitter4s.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class ShowParameters(id: Long,
                          trim_user: Boolean,
                          include_my_retweet: Boolean,
                          include_entities: Boolean) extends Parameters
