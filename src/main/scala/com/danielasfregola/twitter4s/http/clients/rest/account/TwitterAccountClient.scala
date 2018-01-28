package com.danielasfregola.twitter4s.http.clients.rest.account

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.ContributorType.ContributorType
import com.danielasfregola.twitter4s.entities.enums.Hour._
import com.danielasfregola.twitter4s.entities.enums.Language._
import com.danielasfregola.twitter4s.entities.enums.TimeZone._
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.account.parameters.CredentialsParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `account` resource.
  * */
trait TwitterAccountClient {

  protected val restClient: RestClient

  private val accountUrl = s"$apiTwitterUrl/$twitterVersion/account"

  /** Returns settings (including current trend, geo and sleep time information) for the authenticating user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-settings" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-settings</a>.
    *
    * @return : The account settings for the authenticating user.
    * */
  def settings(): Future[RatedData[Settings]] = {
    import restClient._
    Get(s"$accountUrl/settings.json").respondAsRated[Settings]
  }

  /** Updates the authenticating user’s settings.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner</a>.
    *
    * @param allow_contributor_request : Optional, by default it is `None`.
    *                                  Whether to allow others to include user as contributor.
    *                                  Possible values include `All` (anyone can include user),
    *                                  `Following` (only followers can include user) or `None`.
    * @param sleep_time_enabled : Optional, by default it is `None`.
    *                           When set to `true`, will enable sleep time for the user.
    *                           Sleep time is the time when push or SMS notifications should not be sent to the user.
    * @param start_sleep_time : Optional, by default it is `None`.
    *                         The hour that sleep time should begin if it is enabled.
    *                         The time is considered to be in the same timezone as the user’s time_zone setting.
    * @param end_sleep_time : Optional, by default it is `None`.
    *                       The hour that sleep time should end if it is enabled.
    *                       The time is considered to be in the same timezone as the user’s time_zone setting.
    * @param lang : Optional, by default it is `None`.
    *             The language which Twitter should render in for this user.
    * @param time_zone  : Optional, by default it is `None`.
    *                  The timezone dates and times should be displayed in for the user.
    * @param trend_location_woeid : Optional, by default it is `None`.
    *                             The Yahoo! Where On Earth ID to use as the user’s default trend location.
    *                             Global information is available by using 1 as the WOEID.
    *                             The woeid must be one of the locations returned by [node:59].
    * @return : The updated settings.
    * */
  def updateSettings(allow_contributor_request: Option[ContributorType] = None,
                     sleep_time_enabled: Option[Boolean] = None,
                     start_sleep_time: Option[Hour] = None,
                     end_sleep_time: Option[Hour] = None,
                     lang: Option[Language] = None,
                     time_zone: Option[TimeZone] = None,
                     trend_location_woeid: Option[Long] = None): Future[Settings] = {
    val options = SettingsOptions(allow_contributor_request,
                                  sleep_time_enabled,
                                  start_sleep_time,
                                  end_sleep_time,
                                  lang,
                                  time_zone,
                                  trend_location_woeid)
    updateSettings(options)
  }

  /** Updates the authenticating user’s settings.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner</a>.
    *
    * @param settings_options : The setting options to update. Only the parameters specified will be updated.
    * @return : The updated settings.
    * */
  def updateSettings(settings_options: SettingsOptions): Future[Settings] = {
    import restClient._
    Post(s"$accountUrl/settings.json", settings_options).respondAs[Settings]
  }

  /** Returns a representation of the requesting user if authentication was successful; it throws a [[com.danielasfregola.twitter4s.exceptions.TwitterException]] if not.
    * Use this method to test if supplied user credentials are valid.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials</a>.
    *
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @param include_email : By default it is `false`.
    *                      When set to `true` email will be returned in the user objects.
    *                      If the user does not have an email address on their account, or if the email address is un-verified, null will be returned.
    * @return : The user representation.
    * */
  def verifyCredentials(include_entities: Boolean = true,
                        skip_status: Boolean = false,
                        include_email: Boolean = false): Future[RatedData[User]] = {
    import restClient._
    val parameters = CredentialsParameters(include_entities, skip_status, include_email)
    Get(s"$accountUrl/verify_credentials.json", parameters).respondAsRated[User]
  }

  /** Sets the name that users are able to set under the “Account” tab of their settings page.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param name : Full name associated with the profile. Maximum of 20 characters.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The user representation.
    * */
  def updateProfileName(name: String, include_entities: Boolean = true, skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(name = Some(name), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  /** Sets the url that users are able to set under the “Account” tab of their settings page.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param url : URL associated with the profile. Will be prepended with “http://” if not present.
    *            Maximum of 100 characters.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The user representation.
    * */
  def updateProfileUrl(url: String, include_entities: Boolean = true, skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(url = Some(url), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  /** Sets the location that users are able to set under the “Account” tab of their settings page.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param location : The city or country describing where the user of the account is located.
    *                 The contents are not normalized or geocoded in any way. Maximum of 30 characters.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The user representation.
    * */
  def updateProfileLocation(location: String,
                            include_entities: Boolean = true,
                            skip_status: Boolean = false): Future[User] = {
    val update =
      ProfileUpdate(location = Some(location), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  /** Sets the description that users are able to set under the “Account” tab of their settings page.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param description : A description of the user owning the account. Maximum of 160 characters.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The user representation.
    * */
  def updateProfileDescription(description: String,
                               include_entities: Boolean = true,
                               skip_status: Boolean = false): Future[User] = {
    val update =
      ProfileUpdate(description = Some(description), include_entities = include_entities, skip_status = skip_status)
    updateProfile(update)
  }

  /** Sets the link color that users are able to set under the “Account” tab of their settings page.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param link_color : Sets a hex value that controls the color scheme of links used on the authenticating user’s profile page on twitter.com.
    *                   This must be a valid hexadecimal value, and may be either three or six characters (ex: F00 or FF0000).
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The user representation.
    * */
  def updateProfileLinkColor(link_color: String,
                             include_entities: Boolean = true,
                             skip_status: Boolean = false): Future[User] = {
    val update = ProfileUpdate(profile_link_color = Some(link_color),
                               include_entities = include_entities,
                               skip_status = skip_status)
    updateProfile(update)
  }

  /** Sets some values that users are able to set under the “Account” tab of their settings page. Only the parameters specified will be updated.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-update_profile</a>.
    *
    * @param update : The profile values to update.
    * @return : The user representation.
    * */
  def updateProfile(update: ProfileUpdate): Future[User] = {
    import restClient._
    Post(s"$accountUrl/update_profile.json", update).respondAs[User]
  }

  /** Removes the uploaded profile banner for the authenticating user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/post-account-remove_profile_banner</a>.
    * */
  def removeProfileBanner(): Future[Unit] = {
    import restClient._
    Post(s"$accountUrl/remove_profile_banner.json").respondAs[Unit]
  }

  // TODO - support update_profile_backgroung_image
  // TODO - support update_profile_image
  // TODO - support update_profile_banner
}
