package com.danielasfregola.twitter4s.entities

final case class Settings(allow_contributor_request: String,
                          allow_dm_groups_from: String,
                          allow_dms_from: String,
                          always_use_https: Boolean = false,
                          discoverable_by_email: Boolean = false,
                          discoverable_by_mobile_phone: Boolean = false,
                          geo_enabled: Boolean = false,
                          language: String,
                          `protected`: Boolean = false,
                          screen_name: String,
                          sleep_time: SleepTime,
                          show_all_inline_media: Boolean = false,
                          use_cookie_personalization: Boolean = false,
                          time_zone: Option[TimeZoneRecord] = None,
                          trend_location: Seq[Location] = Seq.empty)

final case class SleepTime(enabled: Boolean = false, end_time: Option[String] = None, start_time: Option[String] = None)

final case class TimeZoneRecord(name: String, tzinfo_name: String, utc_offset: Int)

final case class PlaceType(code: String, name: String)
