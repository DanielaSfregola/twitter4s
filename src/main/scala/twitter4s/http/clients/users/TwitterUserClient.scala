package twitter4s.http.clients.users

import scala.concurrent.Future

import twitter4s.entities.User
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.users.parameters.UsersParameters
import twitter4s.util.Configurations

trait TwitterUserClient extends OAuthClient with Configurations {

  val usersUrl = s"$apiTwitterUrl/$twitterVersion/users"

  def users(screen_names: String*): Future[Seq[User]] = users(screen_names)

  def users(screen_names: Seq[String],
            include_entities: Boolean = true): Future[Seq[User]] = {
    require(!screen_names.isEmpty, "please, provide at least one user to lookup")
    val parameters = UsersParameters(user_id = None, Some(screen_names.mkString(",")), include_entities)
    genericUsers(parameters)
  }

  def usersByUserId(ids: Long*): Future[Seq[User]] = usersByUserId(ids)

  def usersByUserId(ids: Seq[Long],
                    include_entities: Boolean = true): Future[Seq[User]] = {
    require(!ids.isEmpty, "please, provide at least one user id to lookup")
    val parameters = UsersParameters(Some(ids.mkString(",")), screen_name = None, include_entities)
    genericUsers(parameters)
  }

  private def genericUsers(parameters: UsersParameters): Future[Seq[User]] =
    Get(s"$usersUrl/lookup.json", parameters).respondAs[Seq[User]]
}
