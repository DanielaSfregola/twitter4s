package twitter4s.http.clients.savedsearches

import scala.concurrent.Future

import twitter4s.entities.SavedSearch
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.savedsearches.parameters.SaveSearchParameters
import twitter4s.util.Configurations

trait TwitterSavedSearchClient extends OAuthClient with Configurations {

  private val savedSearchUrl = s"$apiTwitterUrl/$twitterVersion/saved_searches"

  def savedSearches(): Future[Seq[SavedSearch]] =
    Get(s"$savedSearchUrl/list.json").respondAs[Seq[SavedSearch]]

  def saveSearch(query: String): Future[SavedSearch] = {
    val parameters = SaveSearchParameters(query)
    Post(s"$savedSearchUrl/create.json", parameters).respondAs[SavedSearch]
  }

  def deleteSavedSearch(id: Long): Future[SavedSearch] =
    Post(s"$savedSearchUrl/destroy/$id.json").respondAs[SavedSearch]

  def savedSearch(id: Long): Future[SavedSearch] =
    Get(s"$savedSearchUrl/show/$id.json").respondAs[SavedSearch]
  
}
