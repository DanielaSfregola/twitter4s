package twitter4s.entities

import java.util.Date

case class SavedSearch(created_at: Date,
                       id: Long,
                       id_str: String,
                       name: String,
                       query: String)
