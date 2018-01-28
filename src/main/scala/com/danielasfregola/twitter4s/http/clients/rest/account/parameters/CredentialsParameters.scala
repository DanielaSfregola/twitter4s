package com.danielasfregola.twitter4s.http.clients.rest.account.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class CredentialsParameters(include_entities: Boolean,
                                                          skip_status: Boolean,
                                                          include_email: Boolean)
    extends Parameters
