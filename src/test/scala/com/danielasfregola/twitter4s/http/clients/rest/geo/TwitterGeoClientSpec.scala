package com.danielasfregola.twitter4s.http.clients.rest.geo

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{GeoPlace, GeoSearch}
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterGeoClientSpec extends ClientSpec {

  class TwitterGeoClientSpecContext extends ClientSpecContext with TwitterGeoClient

  "Twitter Geo Client" should {

    "get a geo place by id" in new TwitterGeoClientSpecContext {
      val placeId = "df51dec6f4ee2b2c"
      val result: GeoPlace = when(geoPlace(placeId)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/id/$placeId.json"
      }.respondWith("/twitter/rest/geo/geo_place.json").await
      result === loadJsonAs[GeoPlace]("/fixtures/rest/geo/geo_place.json")
    }

    "perform a reverse geocode search" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(reverseGeocode(-122.42284884, 37.76893497)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/reverse_geocode.json"
        request.uri.queryString() === Some("accuracy=0m&granularity=neighborhood&lat=-122.42284884&long=37.76893497")
      }.respondWith("/twitter/rest/geo/reverse_geocode.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/rest/geo/reverse_geocode.json")
    }

    "search a geo place" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(searchGeoPlace("Creazzo")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
        request.uri.queryString() === Some("query=Creazzo")
      }.respondWith("/twitter/rest/geo/search.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/rest/geo/search.json")
    }

    "perform an advanced search of a geo place" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(advancedSearchGeoPlace(query = Some("Creazzo"), street_address = Some("Via Giotto 15"))).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
        request.uri.queryString() === Some("attribute:street_address=Via+Giotto+15&query=Creazzo")
      }.respondWith("/twitter/rest/geo/advanced_search.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/rest/geo/advanced_search.json")
    }

    "reject advanced search if no latitude or longitude or ip or query have been provided" in new TwitterGeoClientSpecContext {
      val msg = "requirement failed: please, provide at least one of the following: 'latitude', 'longitude', 'query', 'ip'"
      advancedSearchGeoPlace(street_address = Some("Via Giotto 15")) must throwA[IllegalArgumentException](msg)
    }

  }

}
