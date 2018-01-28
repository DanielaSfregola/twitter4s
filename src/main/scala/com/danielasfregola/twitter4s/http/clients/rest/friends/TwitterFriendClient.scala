package com.danielasfregola.twitter4s.http.clients.rest.friends

import com.danielasfregola.twitter4s.entities.{RatedData, UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.friends.parameters.{FriendParameters, FriendsParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `friends` resource.
  * */
trait TwitterFriendClient {

  protected val restClient: RestClient

  private val friendsUrl = s"$apiTwitterUrl/$twitterVersion/friends"

  /** Returns a cursored collection of user IDs for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids</a>.
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
  def friendIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[RatedData[UserIds]] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericGetFriendIds[UserIds](parameters)
  }

  /** Returns a cursored collection of user IDs for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids</a>.
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
  def friendIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[RatedData[UserIds]] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericGetFriendIds[UserIds](parameters)
  }

  /** Returns a cursored collection of user stringified IDs for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids</a>.
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
  def friendStringifiedIdsForUserId(user_id: Long,
                                    cursor: Long = -1,
                                    count: Int = 5000): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericGetFriendIds[UserStringifiedIds](parameters)
  }

  /** Returns a cursored collection of user stringified IDs for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids</a>.
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
  def friendStringifiedIdsForUser(screen_name: String,
                                  cursor: Long = -1,
                                  count: Int = 5000): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericGetFriendIds[UserStringifiedIds](parameters)
  }

  private def genericGetFriendIds[T: Manifest](parameters: FriendParameters): Future[RatedData[T]] = {
    import restClient._
    Get(s"$friendsUrl/ids.json", parameters).respondAsRated[T]
  }

  /** Returns a cursored collection of user objects for every user the specified user is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-list" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-list</a>.
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
                     include_user_entities: Boolean = true): Future[RatedData[Users]] = {
    val parameters =
      FriendsParameters(user_id = None, Some(screen_name), cursor, count, skip_status, include_user_entities)
    genericGetFriends(parameters)
  }

  /** Returns a cursored collection of user objects for every user the specified user id is following (otherwise known as their “friends”).
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-list" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-list</a>.
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
                       include_user_entities: Boolean = true): Future[RatedData[Users]] = {
    val parameters =
      FriendsParameters(Some(user_id), screen_name = None, cursor, count, skip_status, include_user_entities)
    genericGetFriends(parameters)
  }

  private def genericGetFriends(parameters: FriendsParameters): Future[RatedData[Users]] = {
    import restClient._
    Get(s"$friendsUrl/list.json", parameters).respondAsRated[Users]
  }
}
