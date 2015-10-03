package twitter4s.http.clients.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class RetweetersIdsParameters(id: Long,
                                   count: Int,
                                   cursor: Int,
                                   stringify_ids: Boolean) extends Parameters
