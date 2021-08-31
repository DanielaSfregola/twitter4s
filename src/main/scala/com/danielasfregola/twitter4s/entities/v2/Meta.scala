package com.danielasfregola.twitter4s.entities.v2

final case class Meta(result_count: Int,
                      newest_id: Option[String],
                      oldest_id: Option[String],
                      next_token: Option[String],
                      previous_token: Option[String])
