package com.danielasfregola.twitter4s.http.clients.rest.followers

import com.danielasfregola.twitter4s.entities.{RatedData, UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.followers.parameters.{FollowersParameters, FollowingParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `followers` resource.
  * */
trait TwitterFollowerClient {

  protected val restClient: RestClient

  private val followersUrl = s"$apiTwitterUrl/$twitterVersion/followers"

  /** Returns a cursored collection of user IDs for every user following the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the users ids following the specified user.
    * */
  def followerIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = 5000): Future[RatedData[UserIds]] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFollowerIds[UserIds](parameters)
  }

  /** Returns a cursored collection of user IDs for every user following the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the users ids following the specified user.
    * */
  def followerIdsForUser(screen_name: String, cursor: Long = -1, count: Int = 5000): Future[RatedData[UserIds]] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFollowerIds[UserIds](parameters)
  }

  /** Returns a cursored collection of user stringified IDs for every user following the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the users stringified ids following the specified user.
    * */
  def followerStringifiedIdsForUserId(user_id: Long,
                                      cursor: Long = -1,
                                      count: Int = 5000): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFollowerIds[UserStringifiedIds](parameters)
  }

  /** Returns a cursored collection of user stringified IDs for every user following the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-ids</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `5000`.
    *              Specifies the number of IDs attempt retrieval of, up to a maximum of 5,000 per distinct request.
    *              The value of count is best thought of as a limit to the number of results to return.
    *              When using the count parameter with this method, it is wise to use a consistent count value across all requests to the same user’s collection.
    *              Usage of this parameter is encouraged in environments where all 5,000 IDs constitutes too large of a response.
    * @return : The cursored representation of the users stringified ids following the specified user.
    * */
  def followersStringifiedIdsForUser(screen_name: String,
                                     cursor: Long = -1,
                                     count: Int = 5000): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFollowerIds[UserStringifiedIds](parameters)
  }

  private def genericFollowerIds[T: Manifest](parameters: FollowingParameters): Future[RatedData[T]] = {
    import restClient._
    Get(s"$followersUrl/ids.json", parameters).respondAsRated[T]
  }

  /** Returns a cursored collection of user objects for users following the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-list" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-list</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `20`.
    *              The number of users to return per page, up to a maximum of 200.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user objects.
    * @param include_user_entities : By default it is `true`.
    *                              The user object parameters node will not be included when set to false.
    * @return : The cursored representation of the users following the specified user.
    * */
  def followersForUser(screen_name: String,
                       cursor: Long = -1,
                       count: Int = 20,
                       skip_status: Boolean = false,
                       include_user_entities: Boolean = true): Future[RatedData[Users]] = {
    val parameters = FollowersParameters(user_id = None,
                                         screen_name = Some(screen_name),
                                         cursor,
                                         count,
                                         skip_status,
                                         include_user_entities)
    genericGetFollowers(parameters)
  }

  /** Returns a cursored collection of user objects for users following the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-list" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-followers-list</a>.
    *
    * @param user_id : The screen name of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is `20`.
    *              The number of users to return per page, up to a maximum of 200.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user objects.
    * @param include_user_entities : By default it is `true`.
    *                              The user object parameters node will not be included when set to false.
    * @return : The cursored representation of the users following the specified user.
    * */
  def followersForUserId(user_id: Long,
                         cursor: Long = -1,
                         count: Int = 20,
                         skip_status: Boolean = false,
                         include_user_entities: Boolean = true): Future[RatedData[Users]] = {
    val parameters = FollowersParameters(user_id = Some(user_id),
                                         screen_name = None,
                                         cursor,
                                         count,
                                         skip_status,
                                         include_user_entities)
    genericGetFollowers(parameters)
  }

  private def genericGetFollowers(parameters: FollowersParameters): Future[RatedData[Users]] = {
    import restClient._
    Get(s"$followersUrl/list.json", parameters).respondAsRated[Users]
  }
}
