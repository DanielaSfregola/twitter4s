package com.danielasfregola.twitter4s.http.clients.rest.v2.users

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.Expansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.responses.{UserResponse, UsersResponse}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.parameters._
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the v2 `user lookup` resource. */
trait TwitterUserLookupClient {

  protected val restClient: RestClient

  private val userLookupUrl = s"$apiTwitterUrl/$twitterVersionV2/users"

  /** Returns a variety of information about one or more users specified by the requested IDs.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users</a>
    *
    * @param ids         : A comma separated list of user IDs. Up to 100 are allowed in a single request.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a>
    *                    enable you to request additional data objects that relate to the originally
    *                    returned users. The ID that represents the expanded data object will be included directly
    *                    in the user data object, but the expanded object metadata will be returned within the `includes`
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original user object.
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
    *
    * @return : The representation of the search results.
    */
  def lookupUsers(ids: Seq[String],
                  expansions: Seq[Expansions] = Seq.empty[Expansions],
                  tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                  userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[UsersResponse]] = {
    import restClient._

    val parameters = UsersParameters(
      ids,
      expansions,
      tweetFields,
      userFields
    )

    Get(
      s"$userLookupUrl",
      parameters
    ).respondAsRated[UsersResponse]
  }

  /** Returns a variety of information about a single user specified by the requested ID.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-id" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-id</a>
    *
    * @param id          : The ID of the user to lookup.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a>
    *                    enable you to request additional data objects that relate to the originally
    *                    returned users. The ID that represents the expanded data object will be included directly
    *                    in the user data object, but the expanded object metadata will be returned within the `includes`
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original user object.
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
    *
    * @return : The representation of the search results.
    */
  def lookupUser(id: String,
                 expansions: Seq[Expansions] = Seq.empty[Expansions],
                 tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                 userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[UserResponse]] = {
    import restClient._

    val parameters = UserParameters(
      expansions,
      tweetFields,
      userFields
    )

    Get(
      s"$userLookupUrl/$id",
      parameters
    ).respondAsRated[UserResponse]
  }

  /** Returns a variety of information about one or more users specified by their usernames.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by</a>
    *
    * @param usernames   : A comma separated list of Twitter usernames (handles). Up to 100 are allowed in a single request.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a>
    *                    enable you to request additional data objects that relate to the originally
    *                    returned users. The ID that represents the expanded data object will be included directly
    *                    in the user data object, but the expanded object metadata will be returned within the `includes`
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original user object.
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
    *
    * @return : The representation of the search results.
    */
  def lookupUsersByUsernames(usernames: Seq[String],
                             expansions: Seq[Expansions] = Seq.empty[Expansions],
                             tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                             userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[UsersResponse]] = {
    import restClient._

    val parameters = UsersByUsernamesParameters(
      usernames,
      expansions,
      tweetFields,
      userFields
    )

    Get(
      s"$userLookupUrl/by",
      parameters
    ).respondAsRated[UsersResponse]
  }

  /** Returns a variety of information about a single user specified by the requested username.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by-username-username" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by-username-username</a>
    *
    * @param username    : The Twitter username (handle) of the user.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/expansions>Expansions</a>
    *                    enable you to request additional data objects that relate to the originally
    *                    returned users. The ID that represents the expanded data object will be included directly
    *                    in the user data object, but the expanded object metadata will be returned within the `includes`
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original user object.
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
    *
    * @return : The representation of the search results.
    */
  def lookupUserByUsername(username: String,
                           expansions: Seq[Expansions] = Seq.empty[Expansions],
                           tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                           userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[UserResponse]] = {
    import restClient._

    val parameters = UserByUsernameParameters(
      expansions,
      tweetFields,
      userFields
    )

    Get(
      s"$userLookupUrl/by/username/$username",
      parameters
    ).respondAsRated[UserResponse]
  }
}
