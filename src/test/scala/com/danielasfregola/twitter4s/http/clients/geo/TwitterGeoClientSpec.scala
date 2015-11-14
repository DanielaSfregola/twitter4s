package com.danielasfregola.twitter4s.http.clients.geo

import com.danielasfregola.twitter4s.http.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{GeoSearch, GeoPlace}
import com.danielasfregola.twitter4s.http.ClientSpecContext

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

    "perform a reverse geocode search" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(reverseGeocode(-122.42284884, 37.76893497)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/reverse_geocode.json"
        request.uri.query === Query("accuracy=0m&granularity=neighborhood&lat=-122.42284884&long=37.76893497")
      }.respondWith("/twitter/geo/reverse_geocode.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/geo/reverse_geocode.json")
    }

    "search a geo place" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(searchGeoPlace("Creazzo")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
        request.uri.query === Query("query=Creazzo")
      }.respondWith("/twitter/geo/search.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/geo/search.json")
    }

    "perform an advanced search of a geo place" in new TwitterGeoClientSpecContext {
      val result: GeoSearch = when(advancedSearchGeoPlace(query = Some("Creazzo"), street_address = Some("Via Giotto 15"))).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/geo/search.json"
        request.uri.query === Query("attribute:street_address=Via+Giotto+15&query=Creazzo")
      }.respondWith("/twitter/geo/advanced_search.json").await
      result === loadJsonAs[GeoSearch]("/fixtures/geo/advanced_search.json")
    }

    "reject advanced search if no latitude or longitude or ip or query have been provided" in new TwitterGeoClientSpecContext {
      val msg = "requirement failed: please, provide at least one of the following: 'latitude', 'longitude', 'query', 'ip'"
      advancedSearchGeoPlace(street_address = Some("Via Giotto 15")) must throwA[IllegalArgumentException](msg)
    }

  }

}
