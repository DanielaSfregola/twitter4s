package com.danielasfregola.twitter4s.entities

import java.time.Instant

final case class SavedSearch(created_at: Instant, id: Long, id_str: String, name: String, query: String)
