package twitter4s.entities

import java.util.Date

case class Status(
                  created_at: Date,
                  id: Long,
                  id_str: String,
                  text: String,
                  source: String,
                  truncated: Boolean,
                  in_reply_to_status_id: Option[Long],
                  in_reply_to_status_id_str: Option[String],
                  in_reply_to_user_id: Option[Long],
                  in_reply_to_user_id_str: Option[String],
                  in_reply_to_screen_name: Option[String],
                  //user: String, //TODO - User
                  geo: Option[Location],
                  coordinates: Option[Location],
                  place: Option[Place],
                  contributors: Option[String], // TODO - it was null
                  is_quote_status: Boolean,
                  retweet_count: Long,
                  favorite_count: Long,
                  entities: Option[Entities],
                  favorited: Boolean,
                  retweeted: Boolean,
                  lang: String
                 )

