package twitter4s.http.clients.lists.parameters

import twitter4s.entities.enums.Mode.Mode
import twitter4s.http.marshalling.Parameters

case class UpdateListParameters(list_id: Option[Long],
                                slug: Option[String],
                                owner_screen_name: Option[String],
                                owner_id: Option[Long],
                                description: Option[String],
                                name: Option[String],
                                mode: Option[Mode]) extends Parameters
