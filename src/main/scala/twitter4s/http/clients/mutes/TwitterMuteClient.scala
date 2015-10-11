package twitter4s.http.clients.mutes

import scala.concurrent.Future

import twitter4s.entities.{Users, UserIds, User}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.mutes.parameters.{MutedUsersParameters, MutedUsersIdsParameters, MuteParameters}
import twitter4s.util.Configurations

trait TwitterMuteClient extends OAuthClient with Configurations {

  val mutesUrl = s"$apiTwitterUrl/$twitterVersion/mutes/users"

  def muteUser(screen_name: String): Future[User] = {
    val parameters = MuteParameters(user_id = None, Some(screen_name))
    genericMuteUser(parameters)
  }

  def muteUserId(user_id: Long): Future[User] = {
    val parameters = MuteParameters(Some(user_id), screen_name = None)
    genericMuteUser(parameters)
  }

  private def genericMuteUser(parameters: MuteParameters): Future[User] =
    Post(s"$mutesUrl/create.json", parameters).respondAs[User]

  def unmuteUser(screen_name: String): Future[User] = {
    val parameters = MuteParameters(user_id = None, Some(screen_name))
    genericUnmuteUser(parameters)
  }

  def unmuteUserId(user_id: Long): Future[User] = {
    val parameters = MuteParameters(Some(user_id), screen_name = None)
    genericUnmuteUser(parameters)
  }

  private def genericUnmuteUser(parameters: MuteParameters): Future[User] =
    Post(s"$mutesUrl/destroy.json", parameters).respondAs[User]

  def mutedUsersIds(cursor: Long = -1): Future[UserIds] = {
    val parameters = MutedUsersIdsParameters(cursor)
    Get(s"$mutesUrl/ids.json", parameters).respondAs[UserIds]
  }

  def mutedUsers(cursor: Long = -1,
                 include_entities: Boolean = true,
                 skip_status: Boolean = false): Future[Users] = {
    val parameters = MutedUsersParameters(cursor, include_entities, skip_status)
    Get(s"$mutesUrl/list.json", parameters).respondAs[Users]
  }

}
