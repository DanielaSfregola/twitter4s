package twitter4s.http.clients.friendships

import scala.concurrent.Future

import twitter4s.entities.{UserIdsStringified, UserIds}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.friendships.parameters.{FriendshipParameters, FollowingParameters, BlockedParameters}
import twitter4s.util.Configurations


trait TwitterFriendshipClient extends OAuthClient with Configurations {

  val friendshipsUrl = s"$apiTwitterUrl/$twitterVersion/friendships"
  val friendsUrl = s"$apiTwitterUrl/$twitterVersion/friends"
  val followersUrl = s"$apiTwitterUrl/$twitterVersion/followers"

  def blockedUsers(): Future[Seq[Long]] = {
    val parameters = BlockedParameters(stringify_ids = false)
    genericBlockedUsers[Seq[Long]](parameters)
  }

  def blockedUsersStringified(): Future[Seq[String]] = {
    val parameters = BlockedParameters(stringify_ids = true)
    genericBlockedUsers[Seq[String]](parameters)
  }

  private def genericBlockedUsers[T: Manifest](parameters: BlockedParameters): Future[T] =
    Get(s"$friendshipsUrl/no_retweets/ids.json", parameters).respondAs[T]

  def friendsPerUserName(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsPerScreenName(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsPerUserIdStringified(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  def friendsPerUserScreenStringified(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  private def genericFriends[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$friendsUrl/ids.json", parameters).respondAs[T]

  def followersPerUserId(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFollowers[UserIds](parameters)
  }

  def followersPerScreenName(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFollowers[UserIds](parameters)
  }

  def followersPerUserIdStringified(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFollowers[UserIdsStringified](parameters)
  }

  def followersPerScreenNameStringified(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFollowers[UserIdsStringified](parameters)
  }

  private def genericFollowers[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$followersUrl/ids.json", parameters).respondAs[T]

  def incomingFriendships(cursor: Int = -1): Future[UserIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    genericIncomingFriendships[UserIds](parameters)
  }
  
  def incomingFriendshipsStringified(cursor: Int = -1): Future[UserIdsStringified] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    genericIncomingFriendships[UserIdsStringified](parameters)
  }

  private def genericIncomingFriendships[T: Manifest](parameters: FriendshipParameters): Future[T] =
    Get(s"$friendshipsUrl/incoming.json", parameters).respondAs[T]

  def outgoingFriendships(cursor: Int = -1): Future[UserIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    genericOutgoingFriendships[UserIds](parameters)
  }
  def outgoingFriendshipsStringified(cursor: Int = -1): Future[UserIdsStringified] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    genericOutgoingFriendships[UserIdsStringified](parameters)
  }

  private def genericOutgoingFriendships[T: Manifest](parameters: FriendshipParameters): Future[T] =
    Get(s"$friendshipsUrl/outgoing.json", parameters).respondAs[T]


}
