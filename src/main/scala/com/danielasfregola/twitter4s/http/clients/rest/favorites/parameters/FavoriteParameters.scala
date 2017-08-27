package com.danielasfregola.twitter4s.http.clients.rest.favorites.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class FavoriteParameters(id: Long, include_entities: Boolean) extends Parameters
