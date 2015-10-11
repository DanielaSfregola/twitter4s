package twitter4s.http.clients.favorites.parameters

import twitter4s.http.marshalling.Parameters

case class FavoriteParameters(id: Long, include_entities: Boolean) extends Parameters
