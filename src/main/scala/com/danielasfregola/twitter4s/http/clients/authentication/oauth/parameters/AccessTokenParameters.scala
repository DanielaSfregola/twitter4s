package com.danielasfregola.twitter4s.http.clients.authentication.oauth.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class AccessTokenParameters(x_auth_username: Option[String] = None,
                                                          x_auth_password: Option[String] = None,
                                                          x_auth_mode: Option[String] = None,
                                                          oauth_verifier: Option[String] = None,
                                                          oauth_token: Option[String] = None)
    extends Parameters
