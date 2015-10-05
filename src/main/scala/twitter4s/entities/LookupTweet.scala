package twitter4s.entities

import java.util.Date

case class LookupTweet(
                 contributors: Seq[Contributor],
                 coordinates: Option[Coordinates],
                 created_at: Date,
                 entities: Option[Entities],
                 favorite_count: Int = 0,
                 favorited: Boolean = false,
                 id: Long,
                 id_str: String,
                 in_reply_to_screen_name: Option[String],
                 in_reply_to_status_id: Option[Long],
                 in_reply_to_status_id_str: Option[String],
                 in_reply_to_user_id: Option[Long],
                 in_reply_to_user_id_str: Option[String],
                 is_quote_status: Boolean = false,
                 lang: Option[String],
                 place: Option[Place],
                 possibly_sensitive: Boolean = false,
                 retweet_count: Long = 0,
                 retweeted: Boolean = false,
                 source: String,
                 text: String,
                 truncated: Boolean = false,
                 user: Option[LookupUser]
                 )



