package com.danielasfregola.twitter4s.entities

final case class ExtendedTweet(full_text: String,
                               display_text_range: Seq[Int] = Seq.empty,
                               entities: Option[Entities] = None,
                               extended_entities: Option[Entities] = None)
