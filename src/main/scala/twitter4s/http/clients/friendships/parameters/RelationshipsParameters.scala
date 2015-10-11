package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

case class RelationshipsParameters(user_id: Option[String],
                                   screen_name: Option[String]) extends Parameters
