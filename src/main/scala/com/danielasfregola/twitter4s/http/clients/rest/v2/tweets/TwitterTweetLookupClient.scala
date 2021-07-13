package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.Expansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PlaceFields.PlaceFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.PollFields.PollFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.responses.{TweetResponse, TweetsResponse}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.paramaters.{TweetParameters, TweetsParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the v2 `tweet lookup` resource. */
trait TwitterTweetLookupClient {

  protected val restClient: RestClient

  private val tweetLookupUrl = s"$apiTwitterUrl/$twitterVersionV2/tweets"

  /** Returns a variety of information about the Tweet specified by the requested ID or list of IDs.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/api-reference/get-tweets" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/api-reference/get-tweets</a>
    *
    * @param ids         : A comma separated list of Tweet IDs. Up to 100 are allowed in a single request.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    Expansions enable you to request additional data objects that relate to the originally
    *                    returned Tweets. The ID that represents the expanded data object will be included directly
    *                    in the Tweet data object, but the expanded object metadata will be returned within the includes
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original Tweet object.
    * @param tweetFields : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                    will deliver in each returned Tweet object. You can also include the `referenced_tweets.id` expansion
    *                    to return the specified fields for both the original Tweet and any included referenced Tweets.
    *                    The requested Tweet fields will display in both the original Tweet data object, as well as in
    *                    the referenced Tweet expanded data object that will be located in the includes data object.
    * @param userFields  : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                    will deliver in each returned Tweet. While the user ID will be located in the original Tweet object,
    *                    you will find this ID and all additional user fields in the includes data object.
    *
    * @return : The representation of the search results.
    */
  def lookupTweets(ids: Seq[String],
                   expansions: Seq[Expansions] = Seq.empty[Expansions],
                   tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                   userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetsResponse]] = {
    val parameters = TweetsParameters(
      ids,
      expansions,
      Seq.empty[MediaFields], // TODO: Pending addition of media model
      Seq.empty[PlaceFields], // TODO: Pending addition of place model
      Seq.empty[PollFields], // TODO: Pending addition of poll model
      tweetFields,
      userFields
    )

    genericGetTweets(parameters)
  }

  /** Returns a variety of information about a single Tweet specified by the requested ID.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/api-reference/get-tweets-id" target="_blank">
    * https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/api-reference/get-tweets-id</a>
    *
    * @param id          : Unique identifier of the Tweet to request.
    * @param expansions  : Optional, by default is `Seq.empty`
    *                    Expansions enable you to request additional data objects that relate to the originally
    *                    returned Tweets. The ID that represents the expanded data object will be included directly
    *                    in the Tweet data object, but the expanded object metadata will be returned within the includes
    *                    response object, and will also include the ID so that you can match this data object to the
    *                    original Tweet object.
    * @param tweetFields : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/tweet">Tweet fields</a>
    *                    will deliver in each returned Tweet object. You can also include the `referenced_tweets.id` expansion
    *                    to return the specified fields for both the original Tweet and any included referenced Tweets.
    *                    The requested Tweet fields will display in both the original Tweet data object, as well as in
    *                    the referenced Tweet expanded data object that will be located in the includes data object.
    * @param userFields  : Optional, by default is `Seq.empty`
    *                    This <a href="https://developer.twitter.com/en/docs/twitter-api/fields">fields</a> parameter
    *                    enables you to select which specific
    *                    <a href="https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/user">user fields</a>
    *                    will deliver in each returned Tweet. While the user ID will be located in the original Tweet object,
    *                    you will find this ID and all additional user fields in the includes data object.
    *
    * @return : The representation of the tweet.
    */
  def lookupTweet(id: String,
                  expansions: Seq[Expansions] = Seq.empty[Expansions],
                  tweetFields: Seq[TweetFields] = Seq.empty[TweetFields],
                  userFields: Seq[UserFields] = Seq.empty[UserFields]): Future[RatedData[TweetResponse]] = {
    val parameters = TweetParameters(
      expansions,
      Seq.empty[MediaFields], // TODO: Pending addition of media model
      Seq.empty[PlaceFields], // TODO: Pending addition of place model
      Seq.empty[PollFields], // TODO: Pending addition of poll model
      tweetFields,
      userFields
    )

    genericGetTweet(
      id,
      parameters
    )
  }

  private def genericGetTweets(parameters: TweetsParameters): Future[RatedData[TweetsResponse]] = {
    import restClient._
    Get(
      s"$tweetLookupUrl",
      parameters
    ).respondAsRated[TweetsResponse]
  }

  private def genericGetTweet(id: String, parameters: TweetParameters): Future[RatedData[TweetResponse]] = {
    import restClient._
    Get(
      s"$tweetLookupUrl/$id",
      parameters
    ).respondAsRated[TweetResponse]
  }
}
