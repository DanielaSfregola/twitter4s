package com.danielasfregola.twitter4s.http.clients.rest.savedsearches.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class SaveSearchParameters(query: String) extends Parameters
