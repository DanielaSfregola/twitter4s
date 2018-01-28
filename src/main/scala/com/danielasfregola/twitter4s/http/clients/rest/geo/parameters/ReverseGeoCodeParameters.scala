package com.danielasfregola.twitter4s.http.clients.rest.geo.parameters

import com.danielasfregola.twitter4s.entities.Accuracy
import com.danielasfregola.twitter4s.entities.enums.Granularity.Granularity
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class ReverseGeoCodeParameters(lat: Double,
                                                             long: Double,
                                                             accuracy: Accuracy,
                                                             granularity: Granularity,
                                                             max_results: Option[Int],
                                                             callback: Option[String])
    extends Parameters
