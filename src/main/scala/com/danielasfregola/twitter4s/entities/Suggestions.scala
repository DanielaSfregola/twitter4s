package com.danielasfregola.twitter4s.entities

final case class Suggestions(name: String, slug: String, size: Int, users: Seq[User] = Seq.empty)
