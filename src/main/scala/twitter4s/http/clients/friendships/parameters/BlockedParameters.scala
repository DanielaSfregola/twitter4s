package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

case class BlockedParameters(stringify_ids: Boolean) extends Parameters
