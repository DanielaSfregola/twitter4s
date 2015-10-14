package twitter4s.http.clients.lists.parameters

import twitter4s.entities.enums.Mode.Mode
import twitter4s.http.marshalling.Parameters

case class CreateListParameters(name: String,
                                mode: Mode,
                                description: Option[String]) extends Parameters
