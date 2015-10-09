package twitter4s.http.clients.blocks

import scala.concurrent.Future

import twitter4s.entities.Users
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.blocks.parameters.BlockedUsersParameters
import twitter4s.util.Configurations

trait TwitterBlockClient extends OAuthClient with Configurations {

  val blocksUrl = s"$apiTwitterUrl/$twitterVersion/blocks"

  def blockedUsers(include_entities: Boolean = true,
                   skip_status: Boolean = false,
                   cursor: Long = -1): Future[Users] = {
    val parameters = BlockedUsersParameters(include_entities, skip_status, cursor)
    Get(s"$blocksUrl/list.json", parameters).respondAs[Users]
  }
}
