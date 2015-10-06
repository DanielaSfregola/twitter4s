package twitter4s.http.clients.friendships

import scala.concurrent.Future

import twitter4s.entities.{UserIdsStringified, UserIds}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.friendships.parameters.{FollowingParameters, BlockedParameters}
import twitter4s.util.Configurations


trait TwitterFriendshipClient extends OAuthClient with Configurations {

  val friendshipsUrl = s"$apiTwitterUrl/$twitterVersion/friendships"
  val friendUrl = s"$apiTwitterUrl/$twitterVersion/friends"

  def blockedUsers(): Future[Seq[Long]] = {
    val parameters = BlockedParameters(stringify_ids = false)
    genericBlockedUsers[Seq[Long]](parameters)
  }

  def blockedUsersStringified(): Future[Seq[String]] = {
    val parameters = BlockedParameters(stringify_ids = true)
    genericBlockedUsers[Seq[String]](parameters)
  }

  private def genericBlockedUsers[T: Manifest](parameters: BlockedParameters): Future[T] =
    Get(s"$friendshipsUrl/no_retweets/ids.json?$parameters").respondAs[T]

  def friendsById(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsByName(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsStringifiedById(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  def friendsStringifiedByName(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  private def genericFriends[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$friendUrl/ids.json?$parameters").respondAs[T]

}
