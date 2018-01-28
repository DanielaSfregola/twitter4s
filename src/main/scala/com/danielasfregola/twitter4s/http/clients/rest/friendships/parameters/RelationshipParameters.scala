package com.danielasfregola.twitter4s.http.clients.rest.friendships.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

abstract class RelationshipParameters extends Parameters

private[twitter4s] final case class RelationshipParametersByIds(source_id: Long, target_id: Long)
    extends RelationshipParameters

private[twitter4s] final case class RelationshipParametersByNames(source_screen_name: String,
                                                                  target_screen_name: String)
    extends RelationshipParameters
