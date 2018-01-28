package com.danielasfregola.twitter4s.entities

final case class UserMention(id: Long, id_str: String, indices: Seq[Int] = Seq.empty, name: String, screen_name: String)
