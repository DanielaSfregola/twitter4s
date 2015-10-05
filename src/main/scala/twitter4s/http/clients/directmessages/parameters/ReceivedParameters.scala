package twitter4s.http.clients.directmessages.parameters

import twitter4s.http.marshalling.Parameters

case class ReceivedParameters(since_id: Option[Long],
                              max_id: Option[Long],
                              count: Int,
                              include_entities: Boolean,
                              skip_status: Boolean) extends Parameters
