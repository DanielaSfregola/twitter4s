package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

case class FollowParameters(user_id: Option[Long],
                            screen_name: Option[String],
                            follow: Boolean) extends Parameters
