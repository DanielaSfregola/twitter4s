package com.danielasfregola.twitter4s.http.clients.rest.trends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class LocationParameters(lat: Double, long: Double) extends Parameters
