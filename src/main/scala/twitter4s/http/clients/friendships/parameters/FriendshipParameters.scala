package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

case class FriendshipParameters(cursor: Long, stringify_ids: Boolean) extends Parameters
