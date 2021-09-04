package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PlaceFields.PlaceFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PollFields.PollFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.responses.TweetsResponse
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.paramaters.SearchTweetsParameters
import com.danielasfregola.twitter4s.util.Configurations.{apiTwitterUrl, twitterVersionV2}
import java.time.Instant

import scala.concurrent.Future

/** Implements the available requests for the v2 `search tweets` resource. */
trait TwitterSearchTweetsClient {

  protected val restClient: RestClient

  private val tweetSearchUrl = s"$apiTwitterUrl/$twitterVersionV2/tweets/search"

  /** The recent search endpoint returns Tweets from the last seven days that match a search query.
    *
    * The Tweets returned by this endpoint count towards the Project-level
    * <a href="https://developer.twitter.com/en/docs/projects/overview#tweet-cap" target="_blank">
    * Tweet cap</a>.
    *
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-recent" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-recent</a>
    *
    * @param query           : One rule for matching Tweets. If you are using a
    *                        <a href="https://developer.twitter.com/en/docs/projects/overview">Standard Project</a>
    *                        at the Basic
    *                        <a href="https://developer.twitter.com/en/products/twitter-api/early-access/guide#na_1">
    *                          access level</a>, you can use the basic set of
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/search/integrate/build-a-query">
    *                          operators
    *                        </a> and can make queries up to 512 characters long. If you are using an
    *                        <a href="https://developer.twitter.com/en/docs/projects/overview">Academic Research Project</a>
    *                        at the Basic access level, you can use all available operators and can make queries
    *                        up to 1,024 characters long.
    * @param startTime       : Optional, by default is `None`
    *                        The oldest or earliest UTC timestamp from which the Tweets will be provided. Only the 3200 most
    *                        recent Tweets are available. Timestamp is in second granularity and is inclusive
    *                        (for example, 12:00:01 includes the first second of the minute).
    *                        Minimum allowable time is 2010-11-06T00:00:00Z
    *
    *                        Please note that this parameter does not support a millisecond value.
    * @param endTime         : Optional, by default is `None`
    *                        The newest or most recent UTC timestamp from which the Tweets will be provided. Only the 3200
    *                        most recent Tweets are available. Timestamp is in second granularity and is inclusive
    *                        (for example, 12:00:01 includes the first second of the minute).
    *                        Minimum allowable time is 2010-11-06T00:00:01Z
    *
    *                        Please note that this parameter does not support a millisecond value.
    * @param maxResults      : Optional, by default is `None`
    *                        The maximum number of search results to be returned by a request. A number between 10 and 100.
    *                        By default, a request response will return 10 results.
    * @param nextToken       : Optional, by default is `None`
    *                        This parameter is used to get the next 'page' of results. The value used with the parameter
    *                        is pulled directly from the response provided by the API, and should not be modified.
    * @param sinceId         : Optional, by default is `None`
    *                        Returns results with a Tweet ID greater than (that is, more recent than) the specified
    *                        'since' Tweet ID. Only the 3200 most recent Tweets are available. The result will exclude
    *                        the `since_id`. If the limit of Tweets has occurred since the `since_id`,
    *                        the `since_id` will be forced to the oldest ID available.
    * @param untilId         : Optional, by default is `None`
    *                        Returns results with a Tweet ID less less than (that is, older than) the specified 'until'
    *                        Tweet ID. Only the 3200 most recent Tweets are available. The result will exclude the
    *                        `until_id`. If the limit of Tweets has occurred since the `until_id`, the `until_id` will be
    *                        forced to the most recent ID available.
    * @param expansions      : Optional, by default is `Seq.empty`
    *                        Expansions enable you to request additional data objects that relate to the originally
    *                        returned Tweets. The ID that represents the expanded data object will be included directly
    *                        in the Tweet data object, but the expanded object metadata will be returned within the includes
    *                        response object, and will also include the ID so that you can match this data object to the
    *                        original Tweet object.
    * @param mediaFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/media">Media fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return media fields if the Tweet
    *                        contains media and if you've also included the `expansions=attachments.media_keys` query parameter
    *                        in your request. While the media ID will be located in the Tweet object, you will find this
    *                        ID and all additional media fields in the includes data object.
    * @param placeFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/place">place fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return place fields if the Tweet
    *                        contains a place and if you've also included the `expansions=geo.place_id` query parameter
    *                        in your request. While the place ID will be located in the Tweet object, you will find this
    *                        ID and all additional place fields in the includes data object.
    * @param pollFields      : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/poll">poll fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return poll fields if the Tweet
    *                        contains a poll and if you've also included the `expansions=attachments.poll_ids` query parameter
    *                        in your request. While the poll ID will be located in the Tweet object, you will find this
    *                        ID and all additional poll fields in the includes data object.
    * @param tweetFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                        will deliver in each returned Tweet object. You can also include the `referenced_tweets.id` expansion
    *                        to return the specified fields for both the original Tweet and any included referenced Tweets.
    *                        The requested Tweet fields will display in both the original Tweet data object, as well as in
    *                        the referenced Tweet expanded data object that will be located in the includes data object.
    * @param userFields      : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                        will deliver in each returned Tweet. While the user ID will be located in the original Tweet object,
    *                        you will find this ID and all additional user fields in the includes data object.
    *
    * @return : The representation of the search results.
    */
  def searchRecent(query: String,
                   startTime: Option[Instant] = None,
                   endTime: Option[Instant] = None,
                   maxResults: Option[Int] = None,
                   nextToken: Option[String] = None,
                   sinceId: Option[String] = None,
                   untilId: Option[String] = None,
                   expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
                   mediaFields: Seq[MediaFields] = Seq.empty[MediaFields],
                   placeFields: Seq[PlaceFields] = Seq.empty[PlaceFields],
                   pollFields: Seq[PollFields] = Seq.empty[PollFields],
                   tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                   userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetsResponse]] = {
    import restClient._

    val parameters = SearchTweetsParameters(
      query = query,
      start_time = startTime,
      end_time = endTime,
      max_results = maxResults,
      next_token = nextToken,
      since_id = sinceId,
      until_id = untilId,
      expansions = expansions,
      `media.fields` = mediaFields,
      `place.fields` = placeFields,
      `poll.fields` = pollFields,
      `tweet.fields` = tweetFields,
      `user.fields` = userFields
    )

    Get(
      s"$tweetSearchUrl/recent",
      parameters
    ).respondAsRated[TweetsResponse]
  }

  /** This endpoint is only available to those users who have been approved for the
    * <a href="https://developer.twitter.com/en/docs/projects/overview#product-track">Academic Research product track</a>.
    *
    * The full-archive search endpoint returns the complete history of public Tweets matching a search query; since the
    * first Tweet was created March 26, 2006.
    *
    * The Tweets returned by this endpoint count towards the Project-level
    * <a href="https://developer.twitter.com/en/docs/projects/overview#tweet-cap" target="_blank">
    * Tweet cap</a>.
    *
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-all" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-all</a>
    *
    * @param query           : One rule for matching Tweets. If you are using a
    *                        <a href="https://developer.twitter.com/en/docs/projects/overview">Standard Project</a>
    *                        at the Basic
    *                        <a href="https://developer.twitter.com/en/products/twitter-api/early-access/guide#na_1">
    *                          access level</a>, you can use the basic set of
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/search/integrate/build-a-query">
    *                          operators
    *                        </a> and can make queries up to 512 characters long. If you are using an
    *                        <a href="https://developer.twitter.com/en/docs/projects/overview">Academic Research Project</a>
    *                        at the Basic access level, you can use all available operators and can make queries
    *                        up to 1,024 characters long.
    * @param startTime       : Optional, by default is `None`
    *                        The oldest or earliest UTC timestamp from which the Tweets will be provided. Only the 3200 most
    *                        recent Tweets are available. Timestamp is in second granularity and is inclusive
    *                        (for example, 12:00:01 includes the first second of the minute).
    *                        Minimum allowable time is 2010-11-06T00:00:00Z
    *
    *                        Please note that this parameter does not support a millisecond value.
    * @param endTime         : Optional, by default is `None`
    *                        The newest or most recent UTC timestamp from which the Tweets will be provided. Only the 3200
    *                        most recent Tweets are available. Timestamp is in second granularity and is inclusive
    *                        (for example, 12:00:01 includes the first second of the minute).
    *                        Minimum allowable time is 2010-11-06T00:00:01Z
    *
    *                        Please note that this parameter does not support a millisecond value.
    * @param maxResults      : Optional, by default is `None`
    *                        The maximum number of search results to be returned by a request. A number between 10 and 100.
    *                        By default, a request response will return 10 results.
    * @param nextToken       : Optional, by default is `None`
    *                        This parameter is used to get the next 'page' of results. The value used with the parameter
    *                        is pulled directly from the response provided by the API, and should not be modified.
    * @param sinceId         : Optional, by default is `None`
    *                        Returns results with a Tweet ID greater than (that is, more recent than) the specified
    *                        'since' Tweet ID. Only the 3200 most recent Tweets are available. The result will exclude
    *                        the `since_id`. If the limit of Tweets has occurred since the `since_id`,
    *                        the `since_id` will be forced to the oldest ID available.
    * @param untilId         : Optional, by default is `None`
    *                        Returns results with a Tweet ID less less than (that is, older than) the specified 'until'
    *                        Tweet ID. Only the 3200 most recent Tweets are available. The result will exclude the
    *                        `until_id`. If the limit of Tweets has occurred since the `until_id`, the `until_id` will be
    *                        forced to the most recent ID available.
    * @param expansions      : Optional, by default is `Seq.empty`
    *                        Expansions enable you to request additional data objects that relate to the originally
    *                        returned Tweets. The ID that represents the expanded data object will be included directly
    *                        in the Tweet data object, but the expanded object metadata will be returned within the includes
    *                        response object, and will also include the ID so that you can match this data object to the
    *                        original Tweet object.
    * @param mediaFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/media">Media fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return media fields if the Tweet
    *                        contains media and if you've also included the `expansions=attachments.media_keys` query parameter
    *                        in your request. While the media ID will be located in the Tweet object, you will find this
    *                        ID and all additional media fields in the includes data object.
    * @param placeFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/place">place fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return place fields if the Tweet
    *                        contains a place and if you've also included the `expansions=geo.place_id` query parameter
    *                        in your request. While the place ID will be located in the Tweet object, you will find this
    *                        ID and all additional place fields in the includes data object.
    * @param pollFields      : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/poll">poll fields</a>
    *                        will deliver in each returned Tweet. The Tweet will only return poll fields if the Tweet
    *                        contains a poll and if you've also included the `expansions=attachments.poll_ids` query parameter
    *                        in your request. While the poll ID will be located in the Tweet object, you will find this
    *                        ID and all additional poll fields in the includes data object.
    * @param tweetFields     : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                        will deliver in each returned Tweet object. You can also include the `referenced_tweets.id` expansion
    *                        to return the specified fields for both the original Tweet and any included referenced Tweets.
    *                        The requested Tweet fields will display in both the original Tweet data object, as well as in
    *                        the referenced Tweet expanded data object that will be located in the includes data object.
    * @param userFields      : Optional, by default is `Seq.empty`
    *                        This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                        enables you to select which specific
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                        will deliver in each returned Tweet. While the user ID will be located in the original Tweet object,
    *                        you will find this ID and all additional user fields in the includes data object.
    *
    * @return : The representation of the search results.
    */
  def searchAll(query: String,
                startTime: Option[Instant] = None,
                endTime: Option[Instant] = None,
                maxResults: Option[Int] = None,
                nextToken: Option[String] = None,
                sinceId: Option[String] = None,
                untilId: Option[String] = None,
                expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
                mediaFields: Seq[MediaFields] = Seq.empty[MediaFields],
                placeFields: Seq[PlaceFields] = Seq.empty[PlaceFields],
                pollFields: Seq[PollFields] = Seq.empty[PollFields],
                tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetsResponse]] = {
    import restClient._

    val parameters = SearchTweetsParameters(
      query = query,
      start_time = startTime,
      end_time = endTime,
      max_results = maxResults,
      next_token = nextToken,
      since_id = sinceId,
      until_id = untilId,
      expansions = expansions,
      `media.fields` = mediaFields,
      `place.fields` = placeFields,
      `poll.fields` = pollFields,
      `tweet.fields` = tweetFields,
      `user.fields` = userFields
    )

    Get(
      s"$tweetSearchUrl/all",
      parameters
    ).respondAsRated[TweetsResponse]
  }
}
