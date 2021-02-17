package com.danielasfregola.twitter4s.http
package clients.rest.search

import java.time.LocalDate
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.Language._
import com.danielasfregola.twitter4s.entities.enums.ResultType._
import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.entities.enums.{ResultType, TweetMode}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.search.parameters.{
  TweetSearchAllParamaters,
  TweetSearchParameters
}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `search` resource.
  */
trait TwitterSearchClient {

  protected val restClient: RestClient

  private val searchUrl = s"$apiTwitterUrl/$twitterVersion/search"
  private val searchAllUrl = s"$apiTwitterUrl/$twitterVersionNext/search/all"

  /** Returns a collection of relevant Tweets matching a specified query.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/get-search-tweets" target="_blank">
    * https://developer.twitter.com/en/docs/tweets/search/api-reference/get-search-tweets</a>.
    *
    * @param query            : The search query of 500 characters maximum, including operators.
    *                         Queries may additionally be limited by complexity.
    * @param count            : By default it is `15`.
    *                         The number of tweets to return per page, up to a maximum of 100.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will be disincluded when set to `false`.
    * @param result_type      : By default it is `Mixed`.
    *                         Specifies what type of search results you would prefer to receive.
    *                         Valid values include:
    *                    - `Mixed`: Include both popular and real time results in the response.
    *                    - `Recent`: return only the most recent results in the response
    *                    - `Popular`: return only the most popular results in the response.
    * @param geocode          : Optional, by default it is `None`.
    *                         Returns tweets by users located within a given radius of the given latitude/longitude.
    *                         The location is preferentially taking from the Geotagging API, but will fall back to their Twitter profile.
    *                         Note that you cannot use the near operator via the API to geocode arbitrary locations;
    *                         however you can use this geocode parameter to search near geocodes directly.
    *                         A maximum of 1,000 distinct “sub-regions” will be considered when using the radius modifier.
    * @param language         : Optional, by default it is `None`.
    *                         Restricts tweets to the given language. Language detection is best-effort.
    * @param locale           : Optional, by default it is `None`.
    *                         Specify the language of the query you are sending (only `ja` is currently effective).
    *                         This is intended for language-specific consumers and the default should work in the majority of cases.
    * @param until            : Optional, by default it is `None`.
    *                         Returns tweets created before the given date.
    *                         Keep in mind that the search index has a 7-day limit.
    *                         In other words, no tweets will be found for a date older than one week.
    * @param since_id         : Optional, by default it is `None`.
    *                         Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                         There are limits to the number of Tweets which can be accessed through the API.
    *                         If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
    * @param max_id           : Optional, by default it is `None`.
    *                         Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param callback         : Optional, by default it is `None`.
    *                         If supplied, the response will use the JSONP format with a callback of the given name.
    *                         The usefulness of this parameter is somewhat diminished by the requirement of authentication for requests to this endpoint.
    * @param tweet_mode       : Optional, by default it is `Classic`.
    *                         When set to `Extended` prevents tweet text truncating, see https://developer.twitter.com/en/docs/tweets/tweet-updates
    * @return : The representation of the search results.
    */
  def searchTweet(query: String,
                  count: Int = 15,
                  include_entities: Boolean = true,
                  result_type: ResultType = ResultType.Mixed,
                  geocode: Option[GeoCode] = None,
                  language: Option[Language] = None,
                  locale: Option[String] = None,
                  until: Option[LocalDate] = None,
                  since_id: Option[Long] = None,
                  max_id: Option[Long] = None,
                  callback: Option[String] = None,
                  tweet_mode: TweetMode = TweetMode.Classic): Future[RatedData[StatusSearch]] = {
    import restClient._
    val parameters = TweetSearchParameters(
      query,
      count,
      include_entities,
      result_type,
      geocode,
      language,
      locale,
      until,
      since_id,
      max_id,
      callback,
      tweet_mode
    )
    Get(s"$searchUrl/tweets.json", parameters).respondAsRated[StatusSearch]
  }

  /** Returns a collection of relevant tweets matching a query. This endpoint is only available to those using the Academic research product track.
    * For more information, see:
    * <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-all" target="_blank">
    *   https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-all</a>
    *
    * @param query          : One query for matching Tweets. Up to 1024 characters.
    * @param max_results    : Optional. The maximum number of search results to be returned by a request.
    *                       A number between 10 and the system limit (currently 500).
    *                       By default, a request response will return 10 results.
    * @param next_token     : Optional. This parameter is used to get the next 'page' of results.
    *                       The value used with the parameter is pulled directly from the response provided by the API, and should not be modified.
    * @param start_time     : Optional. The oldest UTC timestamp from which the Tweets will be provided.
    *                       Timestamp is in second granularity and is inclusive (for example, 12:00:01 includes the first second of the minute).
    *                       By default, a request will return Tweets from up to 30 days ago if you do not include this parameter.
    * @param end_time       : Optional. The newest, most recent UTC timestamp to which the Tweets will be provided.
    *                       Timestamp is in second granularity and is exclusive (for example, 12:00:01 excludes the first second of the minute).
    *                       If used without start_time, Tweets from 30 days before end_time will be returned by default.
    *                       If not specified, end_time will default to [now - 30 seconds].
    * @return The representation of the search results.
    */
  def searchAllTweet(query: String,
                     max_results: Int = 10,
                     next_token: Option[String],
                     start_time: Option[LocalDate],
                     end_time: Option[LocalDate]): Future[RatedData[StatusSearch]] = {
    import restClient._
    val parameters = TweetSearchAllParamaters(query, max_results, next_token, start_time, end_time)
    Get(s"$searchAllUrl", parameters).respondAsRated[StatusSearch]
  }
}
