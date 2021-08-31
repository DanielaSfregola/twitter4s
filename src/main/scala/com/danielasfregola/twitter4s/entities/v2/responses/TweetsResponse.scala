package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{Error, Meta, Tweet, TweetIncludes}

final case class TweetsResponse(data: Seq[Tweet],
                                includes: Option[TweetIncludes],
                                meta: Option[Meta],
                                errors: Seq[Error])
