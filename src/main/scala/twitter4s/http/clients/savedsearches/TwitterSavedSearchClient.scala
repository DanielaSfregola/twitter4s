package twitter4s.http.clients.savedsearches

import scala.concurrent.Future

import twitter4s.entities.SavedSearch
import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterSavedSearchClient extends OAuthClient with Configurations {

  private val savedSearchUrl = s"$apiTwitterUrl/$twitterVersion/saved_searches"

  def savedSearch(): Future[Seq[SavedSearch]] =
    Get(s"$savedSearchUrl/list.json").respondAs[Seq[SavedSearch]]

  // TODO - GET saved_searches/show/:id
  // TODO - POST saved_searches/create
  // TODO - POST saved_searches/destroy/:id
}
