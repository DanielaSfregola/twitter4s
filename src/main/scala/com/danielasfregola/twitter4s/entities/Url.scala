package com.danielasfregola.twitter4s.entities

final case class Url(indices: Seq[Int] = Seq.empty, url: String, display_url: String, expanded_url: String)
