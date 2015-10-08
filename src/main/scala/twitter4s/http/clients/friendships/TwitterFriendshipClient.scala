package twitter4s.http.clients.friendships

import scala.concurrent.Future

import twitter4s.entities.{Relationship, UserIds, UserIdsStringified, User}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.friendships.parameters._
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

  def friendsPerUserId(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friends(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsPerUserIdStringified(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  def friendsStringified(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFriends[UserIdsStringified](parameters)
  }

  private def genericFriends[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$friendsUrl/ids.json", parameters).respondAs[T]

  def followersPerUserId(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFollowers[UserIds](parameters)
  }

  def followers(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFollowers[UserIds](parameters)
  }

  def followersPerUserIdStringified(user_id: Long, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFollowers[UserIdsStringified](parameters)
  }

  def followersStringified(screen_name: String, cursor: Int = -1, count: Int = -1): Future[UserIdsStringified] = {
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

  def followUserId(user_id: Long, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(Some(user_id), screen_name = None, notify)
    genericFollow(parameters)
  }

  def follow(screen_name: String, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(user_id = None, Some(screen_name), notify)
    genericFollow(parameters)
  }

  private def genericFollow(parameters: FollowParameters): Future[User] =
    Post(s"$friendshipsUrl/create.json", parameters).respondAs[User]

  def unfollow(screen_name: String): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(user_id = None, Some(screen_name))
    genericUnfollow(parameters)
  }

  def unfollow(user_id: Long): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(Some(user_id), screen_name = None)
    genericUnfollow(parameters)
  }

  private def genericUnfollow(parameters: UnfollowParameters): Future[User] =
    Post(s"$friendshipsUrl/destroy.json", parameters).respondAs[User]
  
  def enableRetweetsNotifications(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = true)
    genericNotifications(parameters)
  }

  def enableRetweetsNotifications(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = true)
    genericNotifications(parameters)
  }

  def disableRetweetsNotifications(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = false)
    genericNotifications(parameters)
  }

  def disableRetweetsNotifications(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = false)
    genericNotifications(parameters)
  }

  def enableDeviceNotifications(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = true)
    genericNotifications(parameters)
  }

  def enableDeviceNotifications(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = true)
    genericNotifications(parameters)
  }

  def disableDeviceNotifications(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = false)
    genericNotifications(parameters)
  }

  def disableDeviceNotifications(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = false)
    genericNotifications(parameters)
  }

  private def genericNotifications(parameters: NotificationParameters): Future[Relationship] =
    Post(s"$friendshipsUrl/update.json", parameters).respondAs[Relationship]

  def relationship(source_id: Long, target_id: Long): Future[Relationship] = {
    val parameters = RelationshipParametersByIds(source_id, target_id)
    genericRelationship(parameters)
  }

  def relationship(source_screen_name: String, target_screen_name: String): Future[Relationship] = {
    val parameters = RelationshipParametersByNames(source_screen_name, target_screen_name)
    genericRelationship(parameters)
  }

  private def genericRelationship(parameters: RelationshipParameters): Future[Relationship] =
    Get(s"$friendshipsUrl/show.json", parameters).respondAs[Relationship]
}
