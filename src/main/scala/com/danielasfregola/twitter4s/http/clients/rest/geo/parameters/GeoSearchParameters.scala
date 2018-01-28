package com.danielasfregola.twitter4s.http.clients.rest.geo.parameters

import com.danielasfregola.twitter4s.entities.Accuracy
import com.danielasfregola.twitter4s.entities.enums.Granularity._
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class GeoSearchParameters(lat: Option[Double],
                                                        long: Option[Double],
                                                        query: Option[String],
                                                        ip: Option[String],
                                                        granularity: Option[Granularity],
                                                        accuracy: Option[Accuracy],
                                                        max_results: Option[Int],
                                                        contained_within: Option[String],
                                                        `attribute:street_address`: Option[String],
                                                        callback: Option[String])
    extends Parameters
