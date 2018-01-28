package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class ListTimelineParameters(list_id: Option[Long] = None,
                                                           slug: Option[String] = None,
                                                           owner_screen_name: Option[String] = None,
                                                           owner_id: Option[Long] = None,
                                                           count: Int,
                                                           since_id: Option[Long] = None,
                                                           max_id: Option[Long] = None,
                                                           include_entities: Boolean,
                                                           include_rts: Boolean,
                                                           tweet_mode: TweetMode)
    extends Parameters
