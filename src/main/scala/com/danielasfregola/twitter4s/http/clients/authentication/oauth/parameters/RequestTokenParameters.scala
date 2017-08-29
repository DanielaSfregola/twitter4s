package com.danielasfregola.twitter4s.http.clients.authentication.oauth.parameters

import com.danielasfregola.twitter4s.entities.enums.AccessType.AccessType
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class RequestTokenParameters(x_auth_access_type: Option[AccessType]) extends Parameters
