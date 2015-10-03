package twitter4s.entities

import java.util.Date

import spray.http.Uri
import twitter4s.entities.enums.Language.Language

case class LookupUser(
               contributors_enabled: Boolean = false,
               created_at: Date,
               default_profile: Boolean = false,
               default_profile_image: Boolean = false,
               description: Option[String],
               entities: Option[LookupEntities],
               favourites_count: Int,
               follow_request_sent: Boolean = false,
               following: Boolean = false,
               followers_count: Int,
               friends_count: Int,
               geo_enabled: Boolean = false,
               id: Long,
               id_str: String,
               is_translator: Boolean = false,
               lang: Language,
               listed_count: Int,
               location: Option[String],
               name: String,
               profile_background_color: String,
               profile_background_image_url: Uri,
               profile_background_image_url_https: Uri,
               profile_background_tile: Boolean = false,
               profile_banner_url: Option[Uri],
               profile_image_url: Uri,
               profile_image_url_https: Uri,
               profile_link_color: String,
               profile_sidebar_border_color: String,
               profile_sidebar_fill_color: String,
               profile_text_color: String,
               profile_use_background_image: Option[String],
               `protected`: Boolean = false,
               screen_name: String,
               statuses_count: Int,
               time_zone: Option[String],
               url: Option[Uri],
               utc_offset: Option[Int],
               verified: Boolean = false,
               withheld_in_countries: Option[String],
               withheld_scope: Option[String]
               )
