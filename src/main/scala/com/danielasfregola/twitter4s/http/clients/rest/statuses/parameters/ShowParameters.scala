package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class ShowParameters(id: Long,
                                                   trim_user: Boolean,
                                                   include_my_retweet: Boolean,
                                                   include_entities: Boolean,
                                                   tweet_mode: TweetMode)
    extends Parameters
