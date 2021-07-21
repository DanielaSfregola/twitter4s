package com.danielasfregola.twitter4s.entities.v2

import com.danielasfregola.twitter4s.entities.v2.enums.WithheldScope.WithheldScope

final case class Withheld(copyright: Boolean,
                          country_codes: Seq[String],
                          scope: WithheldScope)
