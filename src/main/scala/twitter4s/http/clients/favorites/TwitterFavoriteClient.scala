package twitter4s.http.clients.favorites

import scala.concurrent.Future

import twitter4s.entities.Status
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.favorites.parameters.{FavoriteParameters, FavoritesParameters}
import twitter4s.util.Configurations

trait TwitterFavoriteClient extends OAuthClient with Configurations {

  private val favoritesUrl = s"$apiTwitterUrl/$twitterVersion/favorites"

  def favorites(screen_name: String,
                count: Int = 20,
                since_id: Option[Long] = None,
                max_id: Option[Long] = None,
                include_entities: Boolean = true): Future[Seq[Status]] = {
    val parameters = FavoritesParameters(user_id = None, Some(screen_name), count, since_id, max_id, include_entities)
    genericFavorites(parameters)
  }

  def favoritesForUserId(user_id: Long,
                         count: Int = 20,
                         since_id: Option[Long] = None,
                         max_id: Option[Long] = None,
                         include_entities: Boolean = true): Future[Seq[Status]] = {
    val parameters = FavoritesParameters(Some(user_id), screen_name = None, count, since_id, max_id, include_entities)
    genericFavorites(parameters)
  }

  private def genericFavorites(parameters: FavoritesParameters): Future[Seq[Status]] =
    Get(s"$favoritesUrl/list.json", parameters).respondAs[Seq[Status]]

  def favorite(id: Long, include_entities: Boolean = true): Future[Status] = {
    val parameters = FavoriteParameters(id, include_entities)
    Post(s"$favoritesUrl/create.json", parameters).respondAs[Status]
  }

  def unfavorite(id: Long, include_entities: Boolean = true): Future[Status] = {
    val parameters = FavoriteParameters(id, include_entities)
    Post(s"$favoritesUrl/destroy.json", parameters).respondAs[Status]
  }
}
