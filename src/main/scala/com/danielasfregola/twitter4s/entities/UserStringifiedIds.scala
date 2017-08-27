package com.danielasfregola.twitter4s.entities

final case class UserStringifiedIds(ids: Seq[String] = Seq.empty, next_cursor: Long, previous_cursor: Long)
