package twitter4s.http.clients.geo

import scala.concurrent.Future

import twitter4s.entities.enums.Granularity
import twitter4s.entities.enums.Granularity._
import twitter4s.entities.{GeoSearch, Accuracy, GeoPlace}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.geo.parameters.{GeoSearchParameters, ReverseGeoCodeParameters}
import twitter4s.util.Configurations

trait TwitterGeoClient  extends OAuthClient with Configurations {

  private val geoUrl = s"$apiTwitterUrl/$twitterVersion/geo"

  def geoPlace(place_id: String): Future[GeoPlace] = Get(s"$geoUrl/id/$place_id.json").respondAs[GeoPlace]

  def reverseGeocode(lat: Double,
                     long: Double,
                     accuracy: Accuracy = Accuracy.Default,
                     granularity: Granularity = Granularity.Neighborhood,
                     max_results: Option[Int] = None,
                     callback: Option[String] = None): Future[GeoSearch] = {
    val parameters = ReverseGeoCodeParameters(lat, long, accuracy, granularity, max_results, callback)
    Get(s"$geoUrl/reverse_geocode.json", parameters).respondAs[GeoSearch]
  }
  
  def searchGeoPlace(query: String): Future[GeoSearch] = advancedSearchGeoPlace(query = Some(query))

  def advancedSearchGeoPlace(latitude: Option[Double] = None,
                             longitude: Option[Double] = None,
                             query: Option[String] = None,
                             ip: Option[String] = None,
                             granularity: Option[Granularity] = None,
                             accuracy: Option[Accuracy] = None,
                             max_results: Option[Int] = None,
                             contained_within: Option[String] = None,
                             street_address: Option[String] = None,
                             callback: Option[String] = None): Future[GeoSearch] = {
    require(latitude.isDefined || longitude.isDefined || ip.isDefined || query.isDefined,
            "please, provide at least one of the following: 'latitude', 'longitude', 'query', 'ip'")
    val parameters = GeoSearchParameters(latitude, longitude, query, ip, granularity, accuracy, max_results, contained_within, street_address, callback)
    Get(s"$geoUrl/search.json", parameters).respondAs[GeoSearch]
  }



}
