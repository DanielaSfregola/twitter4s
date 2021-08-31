package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PlaceFields.PlaceFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PollFields.PollFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.enums.rest.TimelineExclude.TimelineExclude
import com.danielasfregola.twitter4s.entities.v2.responses.TweetsResponse
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.paramaters.{
  TimelineMentionsParameters,
  TimelineTweetsParameters
}
import com.danielasfregola.twitter4s.util.Configurations.{apiTwitterUrl, twitterVersionV2}
import java.time.Instant

import scala.concurrent.Future

/** Implements the available requests for the v2 `timelines` resource. */
trait TwitterTimelinesClient {

  protected val restClient: RestClient

  private val baseTimelinesUrl = s"$apiTwitterUrl/$twitterVersionV2/users"

  /** Returns Tweets composed by a single user, specified by the requested user ID.
    * By default, the most recent ten Tweets are returned per request. Using pagination, the most recent 3,200 Tweets
    * can be retrieved.
    *
    * The Tweets returned by this endpoint count towards the Project-level
    * <a href="https://developer.twitter.com/en/docs/projects/overview#tweet-cap" target="_blank">
    * Tweet cap</a>.
    *
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets</a>
    *
    * @param userId          : Unique identifier of the Twitter account (user ID) for whom to return results.
    *                        User ID can be referenced using the
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/introduction" target="_blank">
    *                        user/lookup</a> endpoint.
    *                        More information on Twitter IDs is
    *                        <a href="https://developer.twitter.com/en/docs/twitter-ids" target="_blank">
    *                        here</a>.
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
    *                        Specifies the number of Tweets to try and retrieve, up to a maximum of 100 per distinct request.
    *                        By default, 10 results are returned if this parameter is not supplied. The minimum permitted
    *                        value is 5. It is possible to receive less than the `max_results` per request throughout the
    *                        pagination process.
    * @param paginationToken : Optional, by default is `None`
    *                        This parameter is used to move forwards or backwards through 'pages' of results, based on
    *                        the value of the `next_token` or `previous_token` in the response. The value used with the
    *                        parameter is pulled directly from the response provided by the API, and should not be modified.
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
    * @param exclude         : Optional, by default is `Seq.empty`
    *                        List of the types of Tweets to exclude from the response. When `exclude=retweets` is used,
    *                        the maximum historical Tweets returned is still 3200. When the `exclude=replies` parameter
    *                        is used for any value, only the most recent 800 Tweets are available.
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
    *                        will deliver in each returned Tweet.
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
  def lookupTimeline(userId: String,
                     startTime: Option[Instant] = None,
                     endTime: Option[Instant] = None,
                     maxResults: Option[Int] = None,
                     paginationToken: Option[String] = None,
                     sinceId: Option[String] = None,
                     untilId: Option[String] = None,
                     exclude: Seq[TimelineExclude] = Seq.empty[TimelineExclude],
                     expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
                     mediaFields: Seq[MediaFields] = Seq.empty[MediaFields],
                     tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                     userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetsResponse]] = {
    val parameters = TimelineTweetsParameters(
      start_time = startTime,
      end_time = endTime,
      max_results = maxResults,
      pagination_token = paginationToken,
      since_id = sinceId,
      until_id = untilId,
      exclude = exclude,
      expansions = expansions,
      `media.fields` = mediaFields,
      `place.fields` = Seq.empty[PlaceFields], // TODO: Pending addition of place model
      `poll.fields` = Seq.empty[PollFields], // TODO: Pending addition of poll fields
      `tweet.fields` = tweetFields,
      `user.fields` = userFields
    )

    genericGetTweets(
      userId,
      parameters
    )
  }

  /** Returns Tweets mentioning a single user specified by the requested user ID. By default, the most recent ten
    * Tweets are returned per request. Using pagination, up to the most recent 800 Tweets can be retrieved.
    *
    * The Tweets returned by this endpoint count towards the Project-level
    * <a href="https://developer.twitter.com/en/docs/projects/overview#tweet-cap" target="_blank">
    * Tweet cap</a>.
    *
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets</a>
    *
    * @param userId          : Unique identifier of the Twitter account (user ID) for whom to return results.
    *                        User ID can be referenced using the
    *                        <a href="https://developer.twitter.com/en/docs/twitter-api/users/lookup/introduction" target="_blank">
    *                        user/lookup</a> endpoint.
    *                        More information on Twitter IDs is
    *                        <a href="https://developer.twitter.com/en/docs/twitter-ids" target="_blank">
    *                        here</a>.
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
    *                        Specifies the number of Tweets to try and retrieve, up to a maximum of 100 per distinct request.
    *                        By default, 10 results are returned if this parameter is not supplied. The minimum permitted
    *                        value is 5. It is possible to receive less than the `max_results` per request throughout the
    *                        pagination process.
    * @param paginationToken : Optional, by default is `None`
    *                        This parameter is used to move forwards or backwards through 'pages' of results, based on
    *                        the value of the `next_token` or `previous_token` in the response. The value used with the
    *                        parameter is pulled directly from the response provided by the API, and should not be modified.
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
  def lookupMentions(userId: String,
                     startTime: Option[Instant] = None,
                     endTime: Option[Instant] = None,
                     maxResults: Option[Int] = None,
                     paginationToken: Option[String] = None,
                     sinceId: Option[String] = None,
                     untilId: Option[String] = None,
                     expansions: Seq[TweetExpansions] = Seq.empty[TweetExpansions],
                     mediaFields: Seq[MediaFields] = Seq.empty[MediaFields],
                     tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                     userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetsResponse]] = {
    val parameters = TimelineMentionsParameters(
      start_time = startTime,
      end_time = endTime,
      max_results = maxResults,
      pagination_token = paginationToken,
      since_id = sinceId,
      until_id = untilId,
      expansions = expansions,
      `media.fields` = mediaFields,
      `place.fields` = Seq.empty[PlaceFields], // TODO: Pending addition of place model
      `poll.fields` = Seq.empty[PollFields], // TODO: Pending addition of poll fields
      `tweet.fields` = tweetFields,
      `user.fields` = userFields
    )

    genericGetMentions(
      userId,
      parameters
    )
  }

  private def genericGetTweets(userId: String,
                               parameters: TimelineTweetsParameters): Future[RatedData[TweetsResponse]] = {
    import restClient._

    Get(
      s"$baseTimelinesUrl/$userId/tweets",
      parameters
    ).respondAsRated[TweetsResponse]
  }

  private def genericGetMentions(userId: String,
                                 parameters: TimelineMentionsParameters): Future[RatedData[TweetsResponse]] = {
    import restClient._

    Get(
      s"$baseTimelinesUrl/$userId/mentions",
      parameters
    ).respondAsRated[TweetsResponse]
  }
}
