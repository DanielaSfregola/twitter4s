package com.danielasfregola.twitter4s.http.clients.rest.favorites.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class FavoritesParameters(user_id: Option[Long],
                                                        screen_name: Option[String],
                                                        count: Int,
                                                        since_id: Option[Long],
                                                        max_id: Option[Long],
                                                        include_entities: Boolean)
    extends Parameters
