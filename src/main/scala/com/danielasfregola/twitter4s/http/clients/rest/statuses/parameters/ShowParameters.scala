package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class ShowParameters(id: Long,
                                             trim_user: Boolean,
                                             include_my_retweet: Boolean,
                                             include_entities: Boolean) extends Parameters
