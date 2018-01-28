package com.danielasfregola.twitter4s.http.clients.rest.geo

import com.danielasfregola.twitter4s.entities.enums.Granularity
import com.danielasfregola.twitter4s.entities.enums.Granularity._
import com.danielasfregola.twitter4s.entities.{Accuracy, GeoPlace, GeoSearch, RatedData}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.geo.parameters.{GeoSearchParameters, ReverseGeoCodeParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `geo` resource.
  * */
trait TwitterGeoClient {

  protected val restClient: RestClient

  private val geoUrl = s"$apiTwitterUrl/$twitterVersion/geo"

  /** Returns all the information about a known place.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/geo/place-information/api-reference/get-geo-id-place_id" target="_blank">
    *   https://developer.twitter.com/en/docs/geo/place-information/api-reference/get-geo-id-place_id</a>.
    *
    * @param place_id : A place id in the world.
    * @return : A set of information about place.
    * */
  def geoPlace(place_id: String): Future[RatedData[GeoPlace]] = {
    import restClient._
    Get(s"$geoUrl/id/$place_id.json").respondAsRated[GeoPlace]
  }

  /** Given a latitude and a longitude, searches for up to 20 places that can be used as a place id when updating a status.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-reverse_geocode" target="_blank">
    *   https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-reverse_geocode</a>.
    *
    * @param latitude : The latitude to search around.
    *                 This parameter will be ignored unless it is inside the range -90.0 to +90.0 (North is positive) inclusive.
    *                 It will also be ignored if there isn’t a corresponding long parameter.
    * @param longitude : The longitude to search around.
    *                  The valid ranges for longitude is -180.0 to +180.0 (East is positive) inclusive.
    *                  This parameter will be ignored if outside that range, if it is not a number,
    *                  if geo_enabled is disabled, or if there not a corresponding lat parameter.
    * @param accuracy : By default it is `Default`, which is 0 meters.
    *                 A hint on the “region” in which to search.
    * @param granularity : By default it is `Neighborhood`
    *                    This is the minimal granularity of place types to return.
    * @param max_results : Optional, by default it is `None`.
    *                    A hint as to the number of results to return.
    *                    This does not guarantee that the number of results returned will equal max_results, but instead informs how many “nearby” results to return.
    *                    Ideally, only pass in the number of places you intend to display to the user here.
    * @param callback : Optional, by default it is `None`.
    *                 If supplied, the response will use the JSONP format with a callback of the given name.
    * @return : The geo search result.
    * */
  def reverseGeocode(latitude: Double,
                     longitude: Double,
                     accuracy: Accuracy = Accuracy.Default,
                     granularity: Granularity = Granularity.Neighborhood,
                     max_results: Option[Int] = None,
                     callback: Option[String] = None): Future[RatedData[GeoSearch]] = {
    import restClient._
    val parameters = ReverseGeoCodeParameters(latitude, longitude, accuracy, granularity, max_results, callback)
    Get(s"$geoUrl/reverse_geocode.json", parameters).respondAsRated[GeoSearch]
  }

  /** Search for places that can be attached to a statuses/update.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-search" target="_blank">
    *   https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-search</a>.
    *
    * @param query : Free-form text to match against while executing a geo-based query, best suited for finding nearby locations by name.
    * @return : The geo search result.
    * */
  def searchGeoPlace(query: String): Future[RatedData[GeoSearch]] = advancedSearchGeoPlace(query = Some(query))

  /** Advanced search for places that can be attached to a statuses/update.
    * Given a latitude and a longitude pair, an IP address, or a name, this request will return a list of all the valid places that can be used as the place_id when updating a status.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-search" target="_blank">
    *   https://developer.twitter.com/en/docs/geo/places-near-location/api-reference/get-geo-search</a>.
    *
    * @param latitude : Optional, by default it is `None`.
    *                 The latitude to search around.
    *                 This parameter will be ignored unless it is inside the range -90.0 to +90.0 (North is positive) inclusive.
    *                 It will also be ignored if there isn’t a corresponding `longitude` parameter.
    * @param longitude : Optional, by default it is `None`.
    *                  The longitude to search around.
    *                  The valid ranges for longitude is -180.0 to +180.0 (East is positive) inclusive.
    *                  This parameter will be ignored if outside that range, if geo_enabled is disabled,
    *                  or if there not a corresponding `latitude` parameter.
    * @param query : Optional, by default it is `None`.
    *              Free-form text to match against while executing a geo-based query, best suited for finding nearby locations by name.
    * @param ip : Optional, by default it is `None`.
    *           An IP address. Used when attempting to fix geolocation based off of the user’s IP address.
    * @param granularity : By default it is `Neighborhood`
    *                    This is the minimal granularity of place types to return.
    * @param max_results : Optional, by default it is `None`.
    *                    A hint as to the number of results to return.
    *                    This does not guarantee that the number of results returned will equal max_results, but instead informs how many “nearby” results to return.
    *                    Ideally, only pass in the number of places you intend to display to the user here.
    * @param contained_within : Optional, by default it is `None`.
    *                         This is the place id which you would like to restrict the search results to.
    *                         Setting this value means only places within the given place id will be found.
    * @param street_address : Optional, by default it is `None`.
    *                       This parameter searches for places which have this given street address.
    *                       There are other well-known, and application specific attributes available.
    *                       Custom attributes are also permitted.
    * @param callback : Optional, by default it is `None`.
    *                 If supplied, the response will use the JSONP format with a callback of the given name.
    * @return : The geo search result.
    * */
  def advancedSearchGeoPlace(latitude: Option[Double] = None,
                             longitude: Option[Double] = None,
                             query: Option[String] = None,
                             ip: Option[String] = None,
                             granularity: Option[Granularity] = None,
                             accuracy: Option[Accuracy] = None,
                             max_results: Option[Int] = None,
                             contained_within: Option[String] = None,
                             street_address: Option[String] = None,
                             callback: Option[String] = None): Future[RatedData[GeoSearch]] = {
    import restClient._
    require(
      latitude.isDefined || longitude.isDefined || ip.isDefined || query.isDefined,
      "please, provide at least one of the following: 'latitude', 'longitude', 'query', 'ip'"
    )
    val parameters = GeoSearchParameters(latitude,
                                         longitude,
                                         query,
                                         ip,
                                         granularity,
                                         accuracy,
                                         max_results,
                                         contained_within,
                                         street_address,
                                         callback)
    Get(s"$geoUrl/search.json", parameters).respondAsRated[GeoSearch]
  }

}
