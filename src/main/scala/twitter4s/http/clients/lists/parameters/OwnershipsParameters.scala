package twitter4s.http.clients.lists.parameters

import twitter4s.http.marshalling.Parameters

case class OwnershipsParameters(user_id: Option[Long],
                                 screen_name: Option[String],
                                 count: Int,
                                 cursor: Long) extends Parameters
