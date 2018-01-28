package com.danielasfregola.twitter4s.http.clients.rest.suggestions

import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.{Category, RatedData, Suggestions, User}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.suggestions.parameters.SuggestionsParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `suggestions` resource.
  */
trait TwitterSuggestionClient {

  protected val restClient: RestClient

  private val suggestionsUrl = s"$apiTwitterUrl/$twitterVersion/users/suggestions"

  /** Access the users in a given category of the Twitter suggested user list.
    * It is recommended that applications cache this data for no more than one hour.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions-slug" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions-slug</a>.
    *
    * @param slug : The short name of list or a category.
    * @param language : By default it is `English`.
    *                  Restricts the suggested categories to the requested language.
    * @return : The representation of the user suggestions.
    */
  def suggestions(slug: String, language: Language = Language.English): Future[RatedData[Suggestions]] = {
    import restClient._
    val parameters = SuggestionsParameters(language)
    Get(s"$suggestionsUrl/$slug.json", parameters).respondAsRated[Suggestions]
  }

  /** Access to Twitter’s suggested user list. This returns the list of suggested user categories.
    * It is recommended that applications cache this data for no more than one hour.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions</a>.
    *
    * @param language : By default it is `English`.
    *                  Restricts the suggested categories to the requested language.
    * @return : The representation of the category suggestions.
    */
  def suggestedCategories(language: Language = Language.English): Future[RatedData[Seq[Category]]] = {
    import restClient._
    val parameters = SuggestionsParameters(language)
    Get(s"$suggestionsUrl.json", parameters).respondAsRated[Seq[Category]]
  }

  /** Access the users in a given category of the Twitter suggested user list and return their most recent status if they are not a protected user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions-slug-members" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-suggestions-slug-members</a>.
    *
    * @param slug : The short name of list or a category.
    * @return : The representation of the suggested users.
    */
  def suggestionsMembers(slug: String): Future[RatedData[Seq[User]]] = {
    import restClient._
    Get(s"$suggestionsUrl/$slug/members.json").respondAsRated[Seq[User]]
  }
}
