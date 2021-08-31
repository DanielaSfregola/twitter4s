package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.paramaters

import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PlaceFields.PlaceFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PollFields.PollFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.http.marshalling.Parameters
import java.time.Instant

private[twitter4s] final case class TimelineMentionsParameters(
    end_time: Option[Instant] = None,
    expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
    max_results: Option[Int] = None,
    `media.fields`: Seq[MediaFields] = Seq.empty[MediaFields],
    pagination_token: Option[String] = None,
    `place.fields`: Seq[PlaceFields] = Seq.empty[PlaceFields],
    `poll.fields`: Seq[PollFields] = Seq.empty[PollFields],
    since_id: Option[String] = None,
    start_time: Option[Instant] = None,
    `tweet.fields`: Seq[TweetFields] = Seq.empty[TweetFields],
    until_id: Option[String] = None,
    `user.fields`: Seq[UserFields] = Seq.empty[UserFields])
    extends Parameters
