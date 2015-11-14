package com.danielasfregola.twitter4s.http.clients.blocks

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{UserStringifiedIds, UserIds, User, Users}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.blocks.parameters.{BlockedUserIdsParameters, BlockParameters, BlockedUsersParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `block` resource.
  */
trait TwitterBlockClient extends OAuthClient with Configurations {

  private val blocksUrl = s"$apiTwitterUrl/$twitterVersion/blocks"

  /** Returns the users that the authenticating user is blocking.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/blocks/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/blocks/list</a>.
    *
    * @param include_entities : by default is `true`.
    *                         The entities node will not be included when set to false.
    * @param skip_status : by default is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @param cursor : by default is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return : representation of blocked users.
    */
  def getBlockedUsers(include_entities: Boolean = true,
                      skip_status: Boolean = false,
                      cursor: Long = -1): Future[Users] = {
    val parameters = BlockedUsersParameters(include_entities, skip_status, cursor)
    Get(s"$blocksUrl/list.json", parameters).respondAs[Users]
  }

  def getBlockedUserIds(cursor: Long = -1): Future[UserIds] = {
    val parameters = BlockedUserIdsParameters(stringify_ids = false, cursor)
    genericGetBlockedUserIds[UserIds](parameters)
  }

  def getBlockedUserStringifiedIds(cursor: Long = -1): Future[UserStringifiedIds] = {
    val parameters = BlockedUserIdsParameters(stringify_ids = true, cursor)
    genericGetBlockedUserIds[UserStringifiedIds](parameters)
  }

  def genericGetBlockedUserIds[T: Manifest](parameters: BlockedUserIdsParameters): Future[T] =
    Get(s"$blocksUrl/ids.json", parameters).respondAs[T]

  def blockUser(screen_name: String,
                include_entities: Boolean = true,
                skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericBlock(parameters)
  }

  def blockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericBlock(parameters)
  }

  private def genericBlock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/create.json", parameters).respondAs[User]

  def unblock(screen_name: String,
            include_entities: Boolean = true,
            skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericUnblock(parameters)
  }

  def unblockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericUnblock(parameters)
  }

  private def genericUnblock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/destroy.json", parameters).respondAs[User]
}
