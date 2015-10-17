package twitter4s.http.clients.geo

import scala.concurrent.Future

import twitter4s.entities.enums.Granularity
import twitter4s.entities.enums.Granularity._
import twitter4s.entities.{GeoSearch, Accuracy, GeoPlace}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.geo.parameters.ReverseGeoCodeParameters
import twitter4s.util.Configurations

trait TwitterGeoClient  extends OAuthClient with Configurations {

  val geoUrl = s"$apiTwitterUrl/$twitterVersion/geo"

  def geoPlace(place_id: String): Future[GeoPlace] = Get(s"$geoUrl/id/$place_id.json").respondAs[GeoPlace]

  def reverseGeocode(lat: Double,
                     long: Double,
                     accuracy: Accuracy = Accuracy.Default,
                     granularity: Granularity = Granularity.Neighborhood,
                     max_results: Option[Int] = None): Future[GeoSearch] = {
    val parameters = ReverseGeoCodeParameters(lat, long, accuracy, granularity, max_results)
    Get(s"$geoUrl/reverse_geocode.json", parameters).respondAs[GeoSearch]
  }

}
