package com.danielasfregola.twitter4s.entities

final case class UserIds(ids: Seq[Long] = Seq.empty, next_cursor: Long, previous_cursor: Long)
