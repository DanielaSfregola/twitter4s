package com.danielasfregola.twitter4s.http.clients.account.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class CredentialsParameters(include_entities: Boolean,
                                 skip_status: Boolean,
                                 include_email: Boolean) extends Parameters
