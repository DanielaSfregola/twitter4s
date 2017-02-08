package com.danielasfregola.twitter4s.http.clients.authentication.oauth.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class AuthenticateParameters(screen_name: Option[String],
                                                     force_login: Boolean) extends Parameters
