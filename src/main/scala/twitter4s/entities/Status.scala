package twitter4s.entities

import java.util.Date

case class Status(
              created_at: Date,
              id: Long,
              id_str: String,
              text: String,
              source: String,
              truncated: Boolean,
              in_reply_to_status_id: Option[Long], // TODO - it was null
              in_reply_to_status_id_str: Option[String], // TODO - it was null
              in_reply_to_user_id: Option[Long], // TODO - is this an optional?
              in_reply_to_user_id_str: Option[String], // TODO - is this an optional?
              in_reply_to_screen_name: Option[String], // TODO - is this an optional?
              //user: String, //TODO - User
              geo: Option[String], // TODO - it was null
              coordinates: Option[String], // TODO - it was null
              place: Option[String], // TODO - it was null
              contributors: Option[String], // TODO - it was null
              is_quote_status: Boolean,
              retweet_count: Long,
              favorite_count: Long,
              //entities: String, // TODO - Entities
              favorited: Boolean,
              retweeted: Boolean,
              lang: String)
