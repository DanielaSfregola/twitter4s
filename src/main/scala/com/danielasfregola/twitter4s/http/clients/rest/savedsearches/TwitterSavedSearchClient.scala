package com.danielasfregola.twitter4s.http.clients.rest.savedsearches

import com.danielasfregola.twitter4s.entities.SavedSearch
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.savedsearches.parameters.SaveSearchParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `saved_searches` resource.
  */
private[twitter4s] trait TwitterSavedSearchClient {

  protected val restClient: RestClient

  private val savedSearchUrl = s"$apiTwitterUrl/$twitterVersion/saved_searches"

  /** Returns the authenticated user’s saved search queries.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/saved_searches/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/saved_searches/list</a>.
    *
    * @return : The sequence of saved searches.
    */
  def savedSearches(): Future[Seq[SavedSearch]] = {
    import restClient._
    Get(s"$savedSearchUrl/list.json").respondAs[Seq[SavedSearch]]
  }

  /** Create a new saved search for the authenticated user.
    * A user may only have 25 saved searches.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/saved_searches/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/saved_searches/create</a>.
    *
    * @param query : The query of the search the user would like to save.
    * @return : The saved search representation.
    */
  def saveSearch(query: String): Future[SavedSearch] = {
    import restClient._
    val parameters = SaveSearchParameters(query)
    Post(s"$savedSearchUrl/create.json", parameters).respondAs[SavedSearch]
  }

  /** Destroys a saved search for the authenticating user.
    * The authenticating user must be the owner of saved search id being destroyed.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/saved_searches/destroy/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/saved_searches/destroy/%3Aid</a>.
    *
    * @return : The deleted search representation.
    */
  def deleteSavedSearch(id: Long): Future[SavedSearch] = {
    import restClient._
    Post(s"$savedSearchUrl/destroy/$id.json").respondAs[SavedSearch]
  }

  /** Retrieve the information for the saved search represented by the given id.
    * The authenticating user must be the owner of saved search ID being requested.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/saved_searches/show/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/saved_searches/show/%3Aid</a>.
    *
    * @return : The saved search representation.
    */
  def savedSearch(id: Long): Future[SavedSearch] = {
    import restClient._
    Get(s"$savedSearchUrl/show/$id.json").respondAs[SavedSearch]
  }
  
}
