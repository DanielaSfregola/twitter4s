package com.danielasfregola.twitter4s.entities

import java.util.Date

final case class SavedSearch(created_at: Date, id: Long, id_str: String, name: String, query: String)
