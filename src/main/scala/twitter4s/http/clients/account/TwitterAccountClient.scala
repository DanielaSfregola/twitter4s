package twitter4s.http.clients.account

import scala.concurrent.Future

import twitter4s.entities.{User, Settings}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.account.parameters.CredentialsParameters
import twitter4s.util.Configurations

trait TwitterAccountClient extends OAuthClient with Configurations {

  val accountUrl = s"$apiTwitterUrl/$twitterVersion/account"

  def settings(): Future[Settings] = Get(s"$accountUrl/settings.json").respondAs[Settings]

  def updateSettings(): Future[Settings] = {
    val parameters =
    Post(s"$accountUrl/settings.json").respondAs[Settings]
  }

  def verifyCredentials(include_entities: Boolean = true,
                        skip_status: Boolean = false,
                        include_email: Boolean = false): Future[User] = {
    val paramaters = CredentialsParameters(include_entities, skip_status, include_email)
    Get(s"$accountUrl/verify_credentials.json", paramaters).respondAs[User]
  }
}
