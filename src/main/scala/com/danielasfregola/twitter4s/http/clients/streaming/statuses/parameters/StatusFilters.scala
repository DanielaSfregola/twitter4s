package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.Language.Language

private[twitter4s] case class StatusFilters(follow: Seq[Long],
                                            track: Seq[String],
                                            locations: Seq[Double],
                                            language: Seq[Language],
                                            stall_warnings: Boolean)
