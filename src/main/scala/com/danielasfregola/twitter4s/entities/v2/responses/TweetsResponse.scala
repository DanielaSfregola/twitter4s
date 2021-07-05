package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{Error, Includes, Tweet}

final case class TweetsResponse(data: Seq[Tweet],
                                includes: Option[Includes],
                                errors: Option[Seq[Error]])
