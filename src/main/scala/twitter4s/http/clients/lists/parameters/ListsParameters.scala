package twitter4s.http.clients.lists.parameters

import twitter4s.http.marshalling.Parameters

case class ListsParameters(user_id: Option[Long],
                           screen_name: Option[String],
                           reverse: Boolean) extends Parameters
