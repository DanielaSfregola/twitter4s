package com.danielasfregola.twitter4s.http.clients.rest.trends

import com.danielasfregola.twitter4s.entities.{Location, LocationTrends, RatedData}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.trends.parameters.{LocationParameters, TrendsParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `trends` resource.
  */
trait TwitterTrendClient {

  protected val restClient: RestClient

  private val trendsUrl = s"$apiTwitterUrl/$twitterVersion/trends"

  /** Returns the top 10 global trending topics.
    * The response is an array of “trend” objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on Twitter Search, and the Twitter Search URL.
    * This information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will count against your rate limit usage.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/trends/trends-for-location/api-reference/get-trends-place" target="_blank">
    *   https://developer.twitter.com/en/docs/trends/trends-for-location/api-reference/get-trends-place</a>.
    *
    * @param exclude_hashtags : By default it is `false`.
    *                         Setting this to `true` will remove all hashtags from the trends list.
    * @return : The representation of the location trends.
    */
  def globalTrends(exclude_hashtags: Boolean = false): Future[RatedData[Seq[LocationTrends]]] =
    trends(1, exclude_hashtags)

  /** Returns the top 10 trending topics for a specific <a href="https://developer.yahoo.com/geo/geoplanet/" target="_blank">WOEID</a>, if trending information is available for it.
    * The response is an array of “trend” objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on Twitter Search, and the Twitter Search URL.
    * This information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will count against your rate limit usage.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/trends/trends-for-location/api-reference/get-trends-place" target="_blank">
    *   https://developer.twitter.com/en/docs/trends/trends-for-location/api-reference/get-trends-place</a>.
    *
    * @param woeid : The <a href="https://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a> of the location to return trending information for.
    *              Global information is available by using 1 as the WOEID.
    * @param exclude_hashtags : By default it is `false`.
    *                         Setting this to `true` will remove all hashtags from the trends list.
    * @return : The representation of the location trends.
    */
  def trends(woeid: Long, exclude_hashtags: Boolean = false): Future[RatedData[Seq[LocationTrends]]] = {
    import restClient._
    val exclude = if (exclude_hashtags) Some("hashtags") else None
    val parameters = TrendsParameters(woeid, exclude)
    Get(s"$trendsUrl/place.json", parameters).respondAsRated[Seq[LocationTrends]]
  }

  /** Returns the locations that Twitter has trending topic information for.
    * The response is an array of “locations” that encode the location’s WOEID and some other human-readable information such as a canonical name and country the location belongs in.
    * A WOEID is a <a href="https://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/trends/locations-with-trending-topics/api-reference/get-trends-available" target="_blank">
    *   https://developer.twitter.com/en/docs/trends/locations-with-trending-topics/api-reference/get-trends-available</a>.
    *
    * @return : The sequence of locations that Twitter has trending topic information for.
    */
  def locationTrends(): Future[RatedData[Seq[Location]]] = {
    import restClient._
    Get(s"$trendsUrl/available.json").respondAsRated[Seq[Location]]
  }

  /** Returns the locations that Twitter has trending topic information for, closest to a specified location.
    * The response is an array of “locations” that encode the location’s WOEID and some other human-readable information such as a canonical name and country the location belongs in.
    * A WOEID is a <a href="https://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/trends/locations-with-trending-topics/api-reference/get-trends-closest" target="_blank">
    *   https://developer.twitter.com/en/docs/trends/locations-with-trending-topics/api-reference/get-trends-closest</a>.
    *
    * @param latitude : If provided with a `longitude` parameter the available trend locations will be sorted by distance, nearest to furthest, to the co-ordinate pair.
    *                 The valid ranges for longitude is -180.0 to +180.0 (West is negative, East is positive) inclusive.
    * @param longitude : If provided with a `latitude` parameter the available trend locations will be sorted by distance, nearest to furthest, to the co-ordinate pair.
    *                  The valid ranges for longitude is -180.0 to +180.0 (West is negative, East is positive) inclusive.
    * @return : The sequence of locations that Twitter has trending topic information for.
    */
  def closestLocationTrends(latitude: Double, longitude: Double): Future[RatedData[Seq[Location]]] = {
    import restClient._
    val parameters = LocationParameters(latitude, longitude)
    Get(s"$trendsUrl/closest.json", parameters).respondAsRated[Seq[Location]]
  }
}
