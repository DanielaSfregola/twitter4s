package twitter4s.http.clients.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class LookupParameters(id: String,
                            include_entities: Boolean,
                            trim_user: Boolean,
                            map: Boolean) extends Parameters
