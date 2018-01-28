package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UserTimelineParameters(user_id: Option[Long],
                                                           screen_name: Option[String],
                                                           since_id: Option[Long],
                                                           count: Int,
                                                           max_id: Option[Long],
                                                           trim_user: Boolean,
                                                           exclude_replies: Boolean,
                                                           contributor_details: Boolean,
                                                           include_rts: Boolean,
                                                           tweet_mode: TweetMode)
    extends Parameters
