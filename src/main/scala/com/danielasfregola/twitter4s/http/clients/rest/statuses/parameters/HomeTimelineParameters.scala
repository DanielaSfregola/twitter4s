package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class HomeTimelineParameters(count: Int,
                                                           since_id: Option[Long],
                                                           max_id: Option[Long],
                                                           trim_user: Boolean,
                                                           exclude_replies: Boolean,
                                                           contributor_details: Boolean,
                                                           include_entities: Boolean,
                                                           tweet_mode: TweetMode)
    extends Parameters
