package twitter4s.entities

import java.util.Date

case class Tweet(contributors: Seq[Contributor] = Seq.empty,
                 coordinates: Option[Coordinates],
                 created_at: Date,
                 current_user_retweet: Option[TweetId],
                 entities: Option[Entities],
                 favorite_count: Int = 0,
                 favorited: Boolean = false,
                 filter_level: Option[String],
                 id: Long,
                 id_str: String,
                 in_reply_to_screen_name: Option[String],
                 in_reply_to_status_id: Option[Long],
                 in_reply_to_status_id_str: Option[String],
                 in_reply_to_user_id: Option[Long],
                 in_reply_to_user_id_str: Option[String],
                 lang: Option[String],
                 place: Option[Place],
                 possibly_sensitive: Boolean = false,
                 quoted_status_id: Option[Long],
                 quoted_status_id_str: Option[String],
                 quoted_status: Option[Tweet],
                 scopes: Map[String, Boolean] = Map.empty,
                 retweet_count: Long = 0,
                 retweeted: Boolean = false,
                 retweeted_status: Option[Tweet],
                 source: String,
                 text: String,
                 truncated: Boolean = false,
                 user: Option[User],
                 withheld_copyright: Boolean = false,
                 withheld_in_countries: Seq[String] = Seq.empty,
                 withheld_scope: Option[String])



