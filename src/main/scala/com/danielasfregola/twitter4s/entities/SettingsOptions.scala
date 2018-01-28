package com.danielasfregola.twitter4s.entities

import com.danielasfregola.twitter4s.entities.enums.ContributorType.ContributorType
import com.danielasfregola.twitter4s.entities.enums.Hour.Hour
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.TimeZone.TimeZone

/**
  * woeid: "Where on Earth Identifiers, by Yahoo!! see http://woeid.rosselliot.co.nz/"
  * */
final case class SettingsOptions(allow_contributor_request: Option[ContributorType] = None,
                                 sleep_time_enabled: Option[Boolean] = None,
                                 start_sleep_time: Option[Hour] = None,
                                 end_sleep_time: Option[Hour] = None,
                                 lang: Option[Language] = None,
                                 time_zone: Option[TimeZone] = None,
                                 trend_location_woeid: Option[Long] = None)
