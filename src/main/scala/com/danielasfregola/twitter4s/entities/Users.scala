package com.danielasfregola.twitter4s.entities

final case class Users(users: Seq[User] = Seq.empty, next_cursor: Long, previous_cursor: Long)
