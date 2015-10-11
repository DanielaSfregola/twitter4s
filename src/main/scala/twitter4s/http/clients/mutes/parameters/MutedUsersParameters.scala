package twitter4s.http.clients.mutes.parameters

import twitter4s.http.marshalling.Parameters

case class MutedUsersParameters(cursor: Long,
                                include_entities: Boolean,
                                skip_status: Boolean) extends Parameters