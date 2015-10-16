package twitter4s.http.clients.geo

import spray.http.HttpMethods
import twitter4s.entities.GeoPlace
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterGeoClientSpec extends ClientSpec {

  trait TwitterGeoClientSpecContext extends ClientSpecContext with TwitterGeoClient

  "Twitter Geo Client" should {

    "get a geo place by id" in new TwitterGeoClientSpecContext {
      val placeId = "df51dec6f4ee2b2c"
      val result: GeoPlace = when(geoPlace(placeId)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/id/$placeId.json"
      }.respondWith("/twitter/geo/geo_place.json").await
      result === loadJsonAs[GeoPlace]("/fixtures/geo/geo_place.json")
    }
  }

}
