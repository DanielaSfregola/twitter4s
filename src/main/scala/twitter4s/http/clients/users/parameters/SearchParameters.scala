package twitter4s.http.clients.users.parameters

import twitter4s.http.marshalling.Parameters

case class SearchParameters(q: String,
                            page: Int,
                            count: Int,
                            include_entities: Boolean) extends Parameters
