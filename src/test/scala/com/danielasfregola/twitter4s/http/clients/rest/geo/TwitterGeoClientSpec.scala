package com.danielasfregola.twitter4s.http.clients.rest.geo

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{GeoPlace, GeoSearch, RatedData}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterGeoClientSpec extends ClientSpec {

  class TwitterGeoClientSpecContext extends RestClientSpecContext with TwitterGeoClient

  "Twitter Geo Client" should {

    "get a geo place by id" in new TwitterGeoClientSpecContext {
      val placeId = "df51dec6f4ee2b2c"
      val result: RatedData[GeoPlace] = when(geoPlace(placeId))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/geo/id/$placeId.json"
        }
        .respondWithRated("/twitter/rest/geo/geo_place.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[GeoPlace]("/fixtures/rest/geo/geo_place.json")
    }

    "perform a reverse geocode search" in new TwitterGeoClientSpecContext {
      val result: RatedData[GeoSearch] = when(reverseGeocode(-122.42284884, 37.76893497))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/geo/reverse_geocode.json"
          request.uri.rawQueryString === Some("accuracy=0m&granularity=neighborhood&lat=-122.42284884&long=37.76893497")
        }
        .respondWithRated("/twitter/rest/geo/reverse_geocode.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[GeoSearch]("/fixtures/rest/geo/reverse_geocode.json")
    }

    "search a geo place" in new TwitterGeoClientSpecContext {
      val result: RatedData[GeoSearch] = when(searchGeoPlace("Creazzo"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
          request.uri.rawQueryString === Some("query=Creazzo")
        }
        .respondWithRated("/twitter/rest/geo/search.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[GeoSearch]("/fixtures/rest/geo/search.json")
    }

    "perform an advanced search of a geo place" in new TwitterGeoClientSpecContext {
      val result: RatedData[GeoSearch] =
        when(advancedSearchGeoPlace(query = Some("Creazzo"), street_address = Some("Via Giotto 20")))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
            request.uri.rawQueryString === Some("attribute:street_address=Via%20Giotto%2020&query=Creazzo")
          }
          .respondWithRated("/twitter/rest/geo/advanced_search.json")
          .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[GeoSearch]("/fixtures/rest/geo/advanced_search.json")
    }

    "reject advanced search if no latitude or longitude or ip or query have been provided" in new TwitterGeoClientSpecContext {
      val msg =
        "requirement failed: please, provide at least one of the following: 'latitude', 'longitude', 'query', 'ip'"
      advancedSearchGeoPlace(street_address = Some("Via Giotto 20")) must throwA[IllegalArgumentException](msg)
    }

  }

}
