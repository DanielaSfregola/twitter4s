package twitter4s.http.clients.account

import scala.concurrent.Future

import twitter4s.entities.enums.ContributorType._
import twitter4s.entities.enums.Hour._
import twitter4s.entities.enums.Language._
import twitter4s.entities.enums.TimeZone._
import twitter4s.entities.{SettingsOptions, User, Settings}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.account.parameters.CredentialsParameters
import twitter4s.util.Configurations

trait TwitterAccountClient extends OAuthClient with Configurations {

  val accountUrl = s"$apiTwitterUrl/$twitterVersion/account"

  def settings(): Future[Settings] = Get(s"$accountUrl/settings.json").respondAs[Settings]

  def updateSettings(allow_contributor_request: Option[ContributorType] = None,
                     sleep_time_enabled: Option[Boolean] = None,
                     start_sleep_time: Option[Hour] = None,
                     end_sleep_time: Option[Hour] = None,
                     lang: Option[Language] = None,
                     time_zone: Option[TimeZone] = None,
                     trend_location_woeid: Option[Long] = None): Future[Settings] = {
    val options = SettingsOptions(allow_contributor_request, sleep_time_enabled, start_sleep_time, end_sleep_time, lang, time_zone, trend_location_woeid)
    updateSettings(options)
  }

  def updateSettings(settingsOptions: SettingsOptions): Future[Settings] =
    Post(s"$accountUrl/settings.json", settingsOptions).respondAs[Settings]

  def verifyCredentials(include_entities: Boolean = true,
                        skip_status: Boolean = false,
                        include_email: Boolean = false): Future[User] = {
    val parameters = CredentialsParameters(include_entities, skip_status, include_email)
    Get(s"$accountUrl/verify_credentials.json", parameters).respondAs[User]
  }
}
