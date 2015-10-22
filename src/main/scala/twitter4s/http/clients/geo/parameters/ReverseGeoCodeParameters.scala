package twitter4s.http.clients.geo.parameters

import twitter4s.entities.Accuracy
import twitter4s.entities.enums.Granularity.Granularity
import twitter4s.http.marshalling.Parameters

case class ReverseGeoCodeParameters(lat: Double,
                                    long: Double,
                                    accuracy: Accuracy,
                                    granularity: Granularity,
                                    max_results: Option[Int],
                                    callback: Option[String]) extends Parameters
