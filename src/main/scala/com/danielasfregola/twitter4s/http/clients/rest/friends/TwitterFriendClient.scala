package com.danielasfregola.twitter4s.http.clients.rest.friends

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.rest.friends.parameters.{FriendsParameters, FriendParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `friends` resource.
  * */
trait TwitterFriendClient extends OAuthClient with Configurations {

  private val friendsUrl = s"$apiTwitterUrl/$twitterVersion/friends"

  /** Returns a cursored collection of user IDs for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/ids</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the user ids the specified user id is following.
    * */
  def friendIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[UserIds] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericGetFriendIds[UserIds](parameters)
  }

  @deprecated("use friendIdsForUserId instead", "2.2")
  def getFriendIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[UserIds] =
    friendIdsForUserId(user_id, cursor, count)

  /** Returns a cursored collection of user IDs for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/ids</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the user ids the specified user is following.
    * */
  def friendIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[UserIds] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericGetFriendIds[UserIds](parameters)
  }

  @deprecated("use friendIdsForUser instead", "2.2")
  def getFriendIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[UserIds] =
    friendIdsForUser(screen_name, cursor, count)

  /** Returns a cursored collection of user stringified IDs for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/ids</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the user stringified ids the specified user id is following.
    * */
  def friendStringifiedIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[UserStringifiedIds] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericGetFriendIds[UserStringifiedIds](parameters)
  }

  @deprecated("use friendStringifiedIdsForUserId instead", "2.2")
  def getFriendStringifiedIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[UserStringifiedIds] =
    friendStringifiedIdsForUserId(user_id, cursor, count)

  /** Returns a cursored collection of user stringified IDs for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/ids</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the user stringified ids the specified user is following.
    * */
  def friendStringifiedIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[UserStringifiedIds] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericGetFriendIds[UserStringifiedIds](parameters)
  }

  @deprecated("use friendStringifiedIdsForUser instead", "2.2")
  def getFriendStringifiedIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[UserStringifiedIds] =
    friendStringifiedIdsForUser(screen_name, cursor, count)

  private def genericGetFriendIds[T: Manifest](parameters: FriendParameters): Future[T] =
    Get(s"$friendsUrl/ids.json", parameters).respondAs[T]

  /** Returns a cursored collection of user objects for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/list</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `20`.
    *              The number of users to return per page, up to a maximum of 200.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user objects.
    * @param include_user_entities : By default it is `true`.
    *                              The user object parameters node will not be included when set to false.
    * @return : The cursored representation of the users the specified user is following.
    * */
  def friendsForUser(screen_name: String,
                     cursor: Long = -1,
                     count: Int = 20,
                     skip_status: Boolean = false,
                     include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FriendsParameters(user_id = None, Some(screen_name), cursor, count, skip_status, include_user_entities)
    genericGetFriends(parameters)
  }

  @deprecated("use friendsForUser instead", "2.2")
  def getFriendsForUser(screen_name: String,
                        cursor: Long = -1,
                        count: Int = 20,
                        skip_status: Boolean = false,
                        include_user_entities: Boolean = true): Future[Users] =
    friendsForUser(screen_name, cursor, count, skip_status, include_user_entities)

  /** Returns a cursored collection of user objects for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/friends/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/friends/list</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `20`.
    *              The number of users to return per page, up to a maximum of 200.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user objects.
    * @param include_user_entities : By default it is `true`.
    *                              The user object parameters node will not be included when set to false.
    * @return : The cursored representation of the users the specified user id is following.
    * */
  def friendsForUserId(user_id: Long,
                       cursor: Long = -1,
                       count: Int = 20,
                       skip_status: Boolean = false,
                       include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FriendsParameters(Some(user_id), screen_name = None, cursor, count, skip_status, include_user_entities)
    genericGetFriends(parameters)
  }

  @deprecated("use friendsForUserId instead", "2.2")
  def getFriendsForUserId(user_id: Long,
                          cursor: Long = -1,
                          count: Int = 20,
                          skip_status: Boolean = false,
                          include_user_entities: Boolean = true): Future[Users] =
    friendsForUserId(user_id, cursor, count, skip_status, include_user_entities)

  private def genericGetFriends(parameters: FriendsParameters): Future[Users] =
    Get(s"$friendsUrl/list.json", parameters).respondAs[Users]
}
