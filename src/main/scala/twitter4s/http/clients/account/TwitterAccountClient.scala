package twitter4s.http.clients.account

import scala.concurrent.Future

import twitter4s.entities.enums.ContributorType.ContributorType
import twitter4s.entities.enums.Hour._
import twitter4s.entities.enums.Language._
import twitter4s.entities.enums.TimeZone._
import twitter4s.entities.{ProfileUpdate, Settings, SettingsOptions, User}
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

  def updateProfileName(name: String,
                        include_entities: Boolean = true,
                        skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(name = Some(name), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  def updateProfileUrl(url: String,
                        include_entities: Boolean = true,
                        skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(url = Some(url), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  def updateProfileLocation(location: String,
                            include_entities: Boolean = true,
                            skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(location = Some(location), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  def updateProfileDescription(description: String,
                               include_entities: Boolean = true,
                               skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(description = Some(description), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  def updateProfileLinkColor(link_color: String,
                             include_entities: Boolean = true,
                             skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(profile_link_color = Some(link_color), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  def updateProfile(update: ProfileUpdate): Future[User] =
    Post(s"$accountUrl/update_profile.json", update).respondAs[User]

  // TODO - support update_profile_backgroung_image
  // TODO - support update_profile_image
  // TODO - support update_profile_banner

  def removeProfileBanner(): Future[Unit] = Post(s"$accountUrl/remove_profile_banner.json").respondAs[Unit]

}
