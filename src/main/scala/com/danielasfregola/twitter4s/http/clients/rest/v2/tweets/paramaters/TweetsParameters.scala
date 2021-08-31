package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.paramaters

import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PlaceFields.PlaceFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PollFields.PollFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class TweetsParameters(ids: Seq[String],
                                                     expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
                                                     `media.fields`: Seq[MediaFields] = Seq.empty[MediaFields],
                                                     `place.fields`: Seq[PlaceFields] = Seq.empty[PlaceFields],
                                                     `poll.fields`: Seq[PollFields] = Seq.empty[PollFields],
                                                     `tweet.fields`: Seq[TweetFields] = Seq.empty[TweetFields],
                                                     `user.fields`: Seq[UserFields] = Seq.empty[UserFields])
    extends Parameters
