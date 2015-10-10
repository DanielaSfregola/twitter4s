package twitter4s.http.clients.blocks.parameters

import twitter4s.http.marshalling.Parameters

case class BlockParameters(user_id: Option[Long],
                           screen_name: Option[String],
                           include_entities: Boolean,
                           skip_status: Boolean) extends Parameters
