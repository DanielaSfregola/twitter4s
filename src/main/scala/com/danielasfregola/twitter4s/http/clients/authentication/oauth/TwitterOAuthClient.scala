package com.danielasfregola.twitter4s.http
package clients.authentication.oauth

import com.danielasfregola.twitter4s.entities.enums.AccessType.AccessType
import com.danielasfregola.twitter4s.entities.{RatedData, StatusSearch}
import com.danielasfregola.twitter4s.http.clients.authentication.AuthenticationClient
import com.danielasfregola.twitter4s.http.clients.authentication.oauth.parameters.{AuthenticateParameters, RequestTokenParameters}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

private[twitter4s] trait TwitterOAuthClient {

  protected val authenticationClient: AuthenticationClient

  private val oauthUrl = s"$apiTwitterUrl/oauth"

  def requestToken(oauth_callback: Option[String] = None, x_auth_access_type: Option[AccessType] = None): Future[StatusSearch] = {
    import authenticationClient._
    val parameters = RequestTokenParameters(x_auth_access_type)
    Post(s"$oauthUrl/request_token", parameters).respondAs[StatusSearch](oauth_callback)
  }

  def authenticate(screen_name: Option[String] = None, force_login: Boolean = false): Future[RatedData[StatusSearch]] = {
    import authenticationClient._
    val parameters = AuthenticateParameters(screen_name, force_login)
    Get(s"$oauthUrl/authenticate", parameters).respondAsRated[StatusSearch]
  }
}
