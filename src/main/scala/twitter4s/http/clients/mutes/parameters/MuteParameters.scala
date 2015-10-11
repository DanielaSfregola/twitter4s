package twitter4s.http.clients.mutes.parameters

import twitter4s.http.marshalling.Parameters

case class MuteParameters(user_id: Option[Long], screen_name: Option[String]) extends Parameters
