package twitter4s.http.clients.lists.parameters

import twitter4s.http.marshalling.Parameters

case class MembersOfListParameters(list_id: Option[Long] = None,
                                   slug: Option[String] = None,
                                   owner_screen_name: Option[String] = None,
                                   owner_id: Option[Long] = None,
                                   count: Int,
                                   cursor: Long,
                                   include_entities: Boolean,
                                   skip_status: Boolean) extends Parameters
