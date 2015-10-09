package twitter4s.entities

import twitter4s.entities.enums.ContributorType.ContributorType
import twitter4s.entities.enums.Hour.Hour
import twitter4s.entities.enums.Language.Language
import twitter4s.entities.enums.TimeZone.TimeZone

/**
 * woeid: "Where on Earth Identifiers, by Yahoo!! see http://woeid.rosselliot.co.nz/"
 * */

case class SettingsConfiguration(allow_contributor_request: Option[ContributorType] = None,
                                 end_sleep_time: Option[Hour] = None,
                                 lang: Option[Language] = None,
                                 sleep_time_enabled: Option[Boolean] = None,
                                 start_sleep_time: Option[Hour] = None,
                                 time_zone: Option[TimeZone] = None,
                                 trend_location_woeid: Option[Long] = None)

