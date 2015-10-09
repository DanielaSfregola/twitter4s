package twitter4s.http.clients.blocks.parameters

import twitter4s.http.marshalling.Parameters

case class BlockedUsersParameters(include_entities: Boolean,
                                  skip_status: Boolean,
                                  cursor: Long) extends Parameters
