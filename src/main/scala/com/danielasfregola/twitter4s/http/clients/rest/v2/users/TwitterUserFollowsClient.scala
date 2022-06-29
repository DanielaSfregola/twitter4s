package com.danielasfregola.twitter4s.http.clients.rest.v2.users

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.UserExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.responses.{UsersPaginatedResponse}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.parameters._
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/**
  * Implements the available requests for the v2 `user follows` resource.
  *
  * (At the moment only the read-only, ie GET, parts)
  *
  * See https://developer.twitter.com/en/docs/twitter-api/users/follows/api-reference for the details.
  * */
trait TwitterUserFollowsClient {

  protected val restClient: RestClient

  // Lookups happen via eg GET /2/users/:id/following or GET /2/users/:id/followers
  private val userBaseUrl = s"$apiTwitterUrl/$twitterVersionV2/users"

  /** Returns the followers of the user identified by the passed user id. Requests and responses may be paginated
    * For more information see https://developer.twitter.com/en/docs/twitter-api/users/follows/api-reference/get-users-id-followers
    *
    * @param id         : The user ID whose followers you would like to retrieve.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a> enable you to request
    *                    additional data objects that relate to the originally returned users. The ID that represents
    *                    the expanded data object will be included directly in the user data object, but the expanded
    *                    object metadata will be returned within the includes response object, and will also include
    *                    the ID so that you can match this data object to the original Tweet object. At this time,
    *                    the only expansion available to endpoints that primarily return user objects is expansions=pinned_tweet_id.
    *                    You will find the expanded Tweet data object living in the includes response object.
    * @param tweetFields : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                    will deliver in each returned pinned Tweet. The Tweet fields will only return if the user has
    *                    a pinned Tweet and if you've also included the `expansions=pinned_tweet_id` expansion in your
    *                    request. While the referenced Tweet ID will be located in the original Tweet object, you will
    *                    find this ID and all additional Tweet fields in the includes data object.
    * @param userFields  : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                    will deliver in each returned User object. These specified user fields will display directly in
    *                    the user data objects.
    * @param maxResults : Optional. The maximum number of results to be returned per page.
    *                     This can be a number between 1 and the 1000. By default, each page will return 100 results.
    * @param paginationToken : Optional, Used to request the next page of results if all results weren't returned with
    *                        the latest request, or to go back to the previous page of results. To return the next page,
    *                        pass the next_token returned in your previous response. To go back one page,
    *                        pass the previous_token returned in your previous response
    *
    * @return : The representation of the query results.
    */
  def followers(id: String,
                maxResults: Option[Int] = None,
                paginationToken: Option[String] = None,
                expansions: Seq[UserExpansions] = Seq.empty[UserExpansions],
                tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                userFields: Seq[UserFields] = Seq.empty[UserFields],
  ): Future[RatedData[UsersPaginatedResponse]] = {
    import restClient._

    val parameters = UserFollowParameters(
      expansions,
      tweetFields,
      userFields,
      maxResults,
      paginationToken
    )

    Get(
      s"$userBaseUrl/$id/followers",
      parameters
    ).respondAsRated[UsersPaginatedResponse]
  }

  /** Returns a list of users the specified user ID is following.
    *
    * For more information see https://developer.twitter.com/en/docs/twitter-api/users/follows/api-reference/get-users-id-following
    *
    * @param id         : The user ID whose following you would like to retrieve.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a> enable you to request
    *                    additional data objects that relate to the originally returned users. The ID that represents
    *                    the expanded data object will be included directly in the user data object, but the expanded
    *                    object metadata will be returned within the includes response object, and will also include
    *                    the ID so that you can match this data object to the original Tweet object. At this time,
    *                    the only expansion available to endpoints that primarily return user objects is expansions=pinned_tweet_id.
    *                    You will find the expanded Tweet data object living in the includes response object.
    * @param tweetFields : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                    will deliver in each returned pinned Tweet. The Tweet fields will only return if the user has
    *                    a pinned Tweet and if you've also included the `expansions=pinned_tweet_id` expansion in your
    *                    request. While the referenced Tweet ID will be located in the original Tweet object, you will
    *                    find this ID and all additional Tweet fields in the includes data object.
    * @param userFields  : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                    will deliver in each returned User object. These specified user fields will display directly in
    *                    the user data objects.
    * @param maxResults : Optional. The maximum number of results to be returned per page.
    *                     This can be a number between 1 and the 1000. By default, each page will return 100 results.
    * @param paginationToken : Optional, Used to request the next page of results if all results weren't returned with
    *                        the latest request, or to go back to the previous page of results. To return the next page,
    *                        pass the next_token returned in your previous response. To go back one page,
    *                        pass the previous_token returned in your previous response
    *
    * @return : The representation of the query results.
    */
  def following(id: String,
                maxResults: Option[Int] = None,
                paginationToken: Option[String] = None,
                expansions: Seq[UserExpansions] = Seq.empty[UserExpansions],
                tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                userFields: Seq[UserFields] = Seq.empty[UserFields],
  ): Future[RatedData[UsersPaginatedResponse]] = {
    import restClient._

    val parameters = UserFollowParameters(
      expansions,
      tweetFields,
      userFields,
      maxResults,
      paginationToken
    )

    Get(
      s"$userBaseUrl/$id/following",
      parameters
    ).respondAsRated[UsersPaginatedResponse]
  }

}
