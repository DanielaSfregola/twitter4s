package com.danielasfregola.twitter4s.http.clients.favorites

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.Status
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.favorites.parameters.{FavoriteParameters, FavoritesParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `favorites` resource.
  * */
trait TwitterFavoriteClient extends OAuthClient with Configurations {

  private val favoritesUrl = s"$apiTwitterUrl/$twitterVersion/favorites"

  /** Returns the 20 most recent Tweets liked by the specified user.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/favorites/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/favorites/list</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    * @param count : By default is `200`.
    *              Specifies the number of records to retrieve.
    *              Must be less than or equal to 200; defaults to 20.
    *              The value of count is best thought of as a limit to the number of tweets to return
    *              because suspended or deleted content is removed after the count has been applied.
    * @param since_id : Optional, by default is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
    * @param max_id : Optional, by default is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default is `true`.
    *                         The entities node will not be included when set to false.
    * @return : The sequence of favorite statuses.
    * */
  def getFavoriteStatusesForUser(screen_name: String,
                                 count: Int = 20,
                                 since_id: Option[Long] = None,
                                 max_id: Option[Long] = None,
                                 include_entities: Boolean = true): Future[Seq[Status]] = {
    val parameters = FavoritesParameters(user_id = None, Some(screen_name), count, since_id, max_id, include_entities)
    genericGetFavoriteStatuses(parameters)
  }

  /** Returns the 20 most recent Tweets liked by the specified user id.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/favorites/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/favorites/list</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    * @param count : By default is `200`.
    *              Specifies the number of records to retrieve.
    *              Must be less than or equal to 200; defaults to 20.
    *              The value of count is best thought of as a limit to the number of tweets to return
    *              because suspended or deleted content is removed after the count has been applied.
    * @param since_id : Optional, by default is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
    * @param max_id : Optional, by default is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default is `true`.
    *                         The entities node will not be included when set to false.
    * @return : The sequence of favorite statuses.
    * */
  def getFavoriteStatusesForUserId(user_id: Long,
                                   count: Int = 20,
                                   since_id: Option[Long] = None,
                                   max_id: Option[Long] = None,
                                   include_entities: Boolean = true): Future[Seq[Status]] = {
    val parameters = FavoritesParameters(Some(user_id), screen_name = None, count, since_id, max_id, include_entities)
    genericGetFavoriteStatuses(parameters)
  }

  private def genericGetFavoriteStatuses(parameters: FavoritesParameters): Future[Seq[Status]] =
    Get(s"$favoritesUrl/list.json", parameters).respondAs[Seq[Status]]

  /** Likes the status specified in the ID parameter as the authenticating user.
    * Note: the like action was known as favorite before November 3, 2015;
    * the historical naming remains in API methods and object properties.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/favorites/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/favorites/create</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param include_entities : By default is `true`.
    *                         The entities node will not be included when set to false.
    * @return : The liked status.
    * */
  def favoriteStatus(id: Long, include_entities: Boolean = true): Future[Status] = {
    val parameters = FavoriteParameters(id, include_entities)
    Post(s"$favoritesUrl/create.json", parameters).respondAs[Status]
  }

  /** Un-likes the status specified in the ID parameter as the authenticating user.
    * Note: the like action was known as favorite before November 3, 2015;
    * the historical naming remains in API methods and object properties.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/favorites/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/favorites/destroy</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param include_entities : By default is `true`.
    *                         The entities node will not be included when set to false.
    * @return : The un-liked status.
    * */
  def unfavoriteStatus(id: Long, include_entities: Boolean = true): Future[Status] = {
    val parameters = FavoriteParameters(id, include_entities)
    Post(s"$favoritesUrl/destroy.json", parameters).respondAs[Status]
  }
}
