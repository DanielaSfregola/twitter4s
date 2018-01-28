package com.danielasfregola.twitter4s.http.clients.rest.mutes

import com.danielasfregola.twitter4s.entities.{RatedData, User, UserIds, Users}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.mutes.parameters.{
  MuteParameters,
  MutedUsersIdsParameters,
  MutedUsersParameters
}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `mutes` resource.
  */
trait TwitterMuteClient {

  protected val restClient: RestClient

  private val mutesUrl = s"$apiTwitterUrl/$twitterVersion/mutes/users"

  /** Mutes the user specified for the authenticating user.
    * Actions taken in this method are asynchronous and changes will be eventually consistent.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-create" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-create</a>.
    *
    * @param screen_name : The screen name of the potentially muted user.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @return : The muted user representation.
    */
  def muteUser(screen_name: String): Future[User] = {
    val parameters = MuteParameters(user_id = None, Some(screen_name))
    genericMuteUser(parameters)
  }

  /** Mutes the user ID specified for the authenticating user.
    * Actions taken in this method are asynchronous and changes will be eventually consistent.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-create"_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-create</a>.
    *
    * @param user_id : The ID of the potentially muted user.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return : The muted user representation.
    */
  def muteUserId(user_id: Long): Future[User] = {
    val parameters = MuteParameters(Some(user_id), screen_name = None)
    genericMuteUser(parameters)
  }

  private def genericMuteUser(parameters: MuteParameters): Future[User] = {
    import restClient._
    Post(s"$mutesUrl/create.json", parameters).respondAs[User]
  }

  /** Un-mutes the user specified for the authenticating user.
    * Actions taken in this method are asynchronous and changes will be eventually consistent.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-destroy" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-destroy</a>.
    *
    * @param screen_name : The screen name of the potentially muted user.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @return : The un-muted user representation.
    */
  def unmuteUser(screen_name: String): Future[User] = {
    val parameters = MuteParameters(user_id = None, Some(screen_name))
    genericUnmuteUser(parameters)
  }

  /** Un-mutes the user ID specified for the authenticating user.
    * Actions taken in this method are asynchronous and changes will be eventually consistent.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-destroy"_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/post-mutes-users-destroy</a>.
    *
    * @param user_id : The ID of the potentially muted user.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return : The un-muted user representation.
    */
  def unmuteUserId(user_id: Long): Future[User] = {
    val parameters = MuteParameters(Some(user_id), screen_name = None)
    genericUnmuteUser(parameters)
  }

  private def genericUnmuteUser(parameters: MuteParameters): Future[User] = {
    import restClient._
    Post(s"$mutesUrl/destroy.json", parameters).respondAs[User]
  }

  /** Returns an array of numeric user ids the authenticating user has muted.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/get-mutes-users-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/get-mutes-users-ids</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of IDs to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out.
    *               The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
    *               See [node:10362, title=”Using cursors to navigate collections”] for more information.
    * @return : The representation of the muted user ids.
    */
  def mutedUserIds(cursor: Long = -1): Future[RatedData[UserIds]] = {
    import restClient._
    val parameters = MutedUsersIdsParameters(cursor)
    Get(s"$mutesUrl/ids.json", parameters).respondAsRated[UserIds]
  }

  /** Returns the users representation that the authenticating user has muted.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/get-mutes-users-list"_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/mute-block-report-users/api-reference/get-mutes-users-list</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of IDs to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out.
    *               The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
    *               See [node:10362, title=”Using cursors to navigate collections”] for more information.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The representation of the muted users.
    */
  def mutedUsers(cursor: Long = -1,
                 include_entities: Boolean = true,
                 skip_status: Boolean = false): Future[RatedData[Users]] = {
    import restClient._
    val parameters = MutedUsersParameters(cursor, include_entities, skip_status)
    Get(s"$mutesUrl/list.json", parameters).respondAsRated[Users]
  }
}
