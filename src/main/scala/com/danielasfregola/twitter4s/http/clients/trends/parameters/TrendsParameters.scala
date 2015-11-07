package com.danielasfregola.twitter4s.http.clients.trends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class TrendsParameters(id: Long, exclude: Option[String] = None) extends Parameters
