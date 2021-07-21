package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{Error, TweetIncludes, Tweet}

final case class TweetsResponse(data: Seq[Tweet],
                                includes: Option[TweetIncludes],
                                errors: Seq[Error])
