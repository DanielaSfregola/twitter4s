package twitter4s.http.clients.account

import scala.concurrent.Future

import twitter4s.entities.Settings
import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterAccountClient extends OAuthClient with Configurations {

  val accountUrl = s"$apiTwitterUrl/$twitterVersion/account"

  def settings(): Future[Settings] = Get(s"$accountUrl/settings.json").respondAs[Settings]
}
