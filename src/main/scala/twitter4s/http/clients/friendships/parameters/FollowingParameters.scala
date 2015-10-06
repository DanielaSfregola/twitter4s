package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

case class FollowingParameters(user_id: Option[Long],
                               screen_name: Option[String],
                               cursor: Int,
                               count: Int,
                               stringify_ids: Boolean) extends Parameters
