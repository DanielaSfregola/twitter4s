package com.danielasfregola.twitter4s.entities.v2

import java.time.Instant

// https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/poll
final case class Poll(id: String,
                      options: Seq[PollOption],
                      duration_minutes: Option[Int],
                      end_datetime: Option[Instant],
                      voting_status: Option[String])

final case class PollOption(position: Int, label: String, votes: Int)
