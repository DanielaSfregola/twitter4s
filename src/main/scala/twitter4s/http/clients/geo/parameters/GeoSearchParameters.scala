package twitter4s.http.clients.geo.parameters

import twitter4s.entities.Accuracy
import twitter4s.entities.enums.Granularity._
import twitter4s.http.marshalling.Parameters

case class GeoSearchParameters(lat: Option[Double],
                               long: Option[Double],
                               query: Option[String],
                               ip: Option[String],
                               granularity: Option[Granularity],
                               accuracy: Option[Accuracy],
                               max_results: Option[Int],
                               contained_within: Option[String],
                               `attribute:street_address`: Option[String],
                               callback: Option[String]) extends Parameters
