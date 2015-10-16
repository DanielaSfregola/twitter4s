package twitter4s.http.clients.geo

import scala.concurrent.Future

import twitter4s.entities.GeoPlace
import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterGeoClient  extends OAuthClient with Configurations {

  val geoUrl = s"$apiTwitterUrl/$twitterVersion/geo"

  def geoPlace(place_id: String): Future[GeoPlace] = Get(s"$geoUrl/id/$place_id.json").respondAs[GeoPlace]

}
