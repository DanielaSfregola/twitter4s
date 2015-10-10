package twitter4s.http.clients.users.parameters

import twitter4s.http.marshalling.Parameters

case class UsersParameters(user_id: Option[String],
                           screen_name: Option[String],
                           include_entities: Boolean) extends Parameters
