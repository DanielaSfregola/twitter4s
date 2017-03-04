package com.danielasfregola.twitter4s.http
package clients.authentication.oauth

import com.danielasfregola.twitter4s.entities.authentication.OAuthToken
import com.danielasfregola.twitter4s.entities.enums.AccessType.AccessType
import com.danielasfregola.twitter4s.http.clients.authentication.AuthenticationClient
import com.danielasfregola.twitter4s.http.clients.authentication.oauth.parameters.RequestTokenParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `oauth` resource.
  * */
trait TwitterOAuthClient {

  protected val authenticationClient: AuthenticationClient

  private val oauthUrl = s"$apiTwitterUrl/oauth"

  /** Allows a Consumer application to obtain an OAuth Request Token to request user authorization.
    * This method fulfills <a href="http://oauth.net/core/1.0/#auth_step1">Section 6.1</a> of the
    * <a href="http://oauth.net/core/1.0/#anchor9">OAuth 1.0 authentication flow</a>.
    * For more information see
    * <a href="https://dev.twitter.com/oauth/reference/post/oauth/request_token" target="_blank">
    *   https://dev.twitter.com/oauth/reference/post/oauth/request_token</a>.
    *
    * @param oauth_callback : Optional, by default is `None`.
    *                         For OAuth 1.0a compliance this parameter is required.
    *                         The value you specify here will be used as the URL a user is redirected
    *                         to should they approve your application’s access to their account.
    *                         Set this to oob for out-of-band pin mode. This is also how you specify
    *                         custom callbacks for use in desktop/mobile applications.
    * @param x_auth_access_type : Optional, by default is `None`.
    *                             Overrides the access level an application requests to a users account.
    *                             Supported values are read or write . This parameter is intended to
    *                             allow a developer to register a read/write application but also request
    *                             read only access when appropriate.
    * @return : The authentication token.
    * */
  def requestToken(oauth_callback: Option[String] = None, x_auth_access_type: Option[AccessType] = None): Future[OAuthToken] = {
    import authenticationClient._
    val parameters = RequestTokenParameters(x_auth_access_type)
    Post(s"$oauthUrl/request_token", parameters).respondAs[OAuthToken](oauth_callback)
  }
}
