package com.danielasfregola.twitter4s.http.clients.rest.savedsearches

import com.danielasfregola.twitter4s.entities.{RatedData, SavedSearch}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.savedsearches.parameters.SaveSearchParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `saved_searches` resource.
  */
trait TwitterSavedSearchClient {

  protected val restClient: RestClient

  private val savedSearchUrl = s"$apiTwitterUrl/$twitterVersion/saved_searches"

  /** Returns the authenticated user’s saved search queries.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/get-saved_searches-list" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/search/api-reference/get-saved_searches-list</a>.
    *
    * @return : The sequence of saved searches.
    */
  def savedSearches(): Future[RatedData[Seq[SavedSearch]]] = {
    import restClient._
    Get(s"$savedSearchUrl/list.json").respondAsRated[Seq[SavedSearch]]
  }

  /** Create a new saved search for the authenticated user.
    * A user may only have 25 saved searches.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/post-saved_searches-create" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/search/api-reference/post-saved_searches-create</a>.
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
    * <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/post-saved_searches-destroy-id" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/search/api-reference/post-saved_searches-destroy-id</a>.
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
    * <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/get-saved_searches-show-id" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/search/api-reference/get-saved_searches-show-id</a>.
    *
    * @return : The saved search representation.
    */
  def savedSearch(id: Long): Future[RatedData[SavedSearch]] = {
    import restClient._
    Get(s"$savedSearchUrl/show/$id.json").respondAsRated[SavedSearch]
  }

}
