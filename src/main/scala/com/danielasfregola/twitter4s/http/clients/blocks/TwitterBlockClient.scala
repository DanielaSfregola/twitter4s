package com.danielasfregola.twitter4s.http.clients.blocks

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{UserStringifiedIds, UserIds, User, Users}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.blocks.parameters.{BlockedUserIdsParameters, BlockParameters, BlockedUsersParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `blocks` resource.
  */
trait TwitterBlockClient extends OAuthClient with Configurations {

  private val blocksUrl = s"$apiTwitterUrl/$twitterVersion/blocks"

  /** Returns the users that the authenticating user is blocking.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/blocks/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/blocks/list</a>.
    *
    * @param include_entities : By default it is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return : The cursored representation of blocked users.
    */
  def getBlockedUsers(include_entities: Boolean = true,
                      skip_status: Boolean = false,
                      cursor: Long = -1): Future[Users] = {
    val parameters = BlockedUsersParameters(include_entities, skip_status, cursor)
    Get(s"$blocksUrl/list.json", parameters).respondAs[Users]
  }

  /** Returns an array of user ids the authenticating user is blocking.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/blocks/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/blocks/ids</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return : The cursored representation of user ids.
    */
  def getBlockedUserIds(cursor: Long = -1): Future[UserIds] = {
    val parameters = BlockedUserIdsParameters(stringify_ids = false, cursor)
    genericGetBlockedUserIds[UserIds](parameters)
  }

  /** Returns an array of user stringified ids the authenticating user is blocking.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/blocks/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/blocks/ids</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return : The cursored representation of user stringified ids with cursors.
    */
  def getBlockedUserStringifiedIds(cursor: Long = -1): Future[UserStringifiedIds] = {
    val parameters = BlockedUserIdsParameters(stringify_ids = true, cursor)
    genericGetBlockedUserIds[UserStringifiedIds](parameters)
  }

  private def genericGetBlockedUserIds[T: Manifest](parameters: BlockedUserIdsParameters): Future[T] =
    Get(s"$blocksUrl/ids.json", parameters).respondAs[T]

  /** Blocks the specified user from following the authenticating user.
    * In addition the blocked user will not show in the authenticating users mentions or timeline (unless retweeted by another user).
    * If a follow or friend relationship exists it is destroyed.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/blocks/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/blocks/create</a>.
    *
    * @param screen_name : The screen name of the potentially blocked user.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The representation of the blocked user.
    */
  def blockUser(screen_name: String,
                include_entities: Boolean = true,
                skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericBlock(parameters)
  }

  /** Blocks the specified user id from following the authenticating user.
    * In addition the blocked user will not show in the authenticating users mentions or timeline (unless retweeted by another user).
    * If a follow or friend relationship exists it is destroyed.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/blocks/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/blocks/create</a>.
    *
    * @param user_id : The ID of the potentially blocked user.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The representation of the blocked user.
    */
  def blockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericBlock(parameters)
  }

  private def genericBlock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/create.json", parameters).respondAs[User]

  /** Un-blocks the user for the authenticating user.
    * Returns the un-blocked user in the requested format when successful.
    * If relationships existed before the block was instated, they will not be restored.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/blocks/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/blocks/destroy</a>.
    *
    * @param screen_name : The screen name of the potentially blocked user.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The representation of the unblocked user.
    */
  def unblockUser(screen_name: String,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericUnblock(parameters)
  }

  /** Un-blocks the user specified id for the authenticating user.
    * Returns the un-blocked user in the requested format when successful.
    * If relationships existed before the block was instated, they will not be restored.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/blocks/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/blocks/destroy</a>.
    *
    * @param user_id : The ID of the potentially blocked user.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The representation of the unblocked user.
    */
  def unblockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericUnblock(parameters)
  }

  private def genericUnblock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/destroy.json", parameters).respondAs[User]
}
