package twitter4s.http.clients.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class RetweetersIdsParameters(id: Long,
                                   count: Int,
                                   cursor: Long,
                                   stringify_ids: Boolean) extends Parameters
