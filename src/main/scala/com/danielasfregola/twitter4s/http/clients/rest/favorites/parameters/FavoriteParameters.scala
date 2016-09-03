package com.danielasfregola.twitter4s.http.clients.rest.favorites.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class FavoriteParameters(id: Long, include_entities: Boolean) extends Parameters
