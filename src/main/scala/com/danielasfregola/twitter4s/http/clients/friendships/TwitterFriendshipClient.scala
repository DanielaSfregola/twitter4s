package com.danielasfregola.twitter4s.http.clients.friendships

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.friendships.parameters._
import com.danielasfregola.twitter4s.util.Configurations


trait TwitterFriendshipClient extends OAuthClient with Configurations {

  private val friendshipsUrl = s"$apiTwitterUrl/$twitterVersion/friendships"

  def getNoRetweetsUserIds(): Future[Seq[Long]] = {
    val parameters = BlockedParameters(stringify_ids = false)
    genericGetNoRetweetsUserIds[Seq[Long]](parameters)
  }

  def getNoRetweetsUserStringifiedIds(): Future[Seq[String]] = {
    val parameters = BlockedParameters(stringify_ids = true)
    genericGetNoRetweetsUserIds[Seq[String]](parameters)
  }

  private def genericGetNoRetweetsUserIds[T: Manifest](parameters: BlockedParameters): Future[T] =
    Get(s"$friendshipsUrl/no_retweets/ids.json", parameters).respondAs[T]

  def getIncomingFriendshipIds(cursor: Long = -1): Future[UserIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    genericGetIncomingFriendships[UserIds](parameters)
  }
  
  def getIncomingFriendshipStringifiedIds(cursor: Long = -1): Future[UserStringifiedIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    genericGetIncomingFriendships[UserStringifiedIds](parameters)
  }

  private def genericGetIncomingFriendships[T: Manifest](parameters: FriendshipParameters): Future[T] =
    Get(s"$friendshipsUrl/incoming.json", parameters).respondAs[T]

  def getOutgoingFriendshipIds(cursor: Long = -1): Future[UserIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    getGenericOutgoingFriendships[UserIds](parameters)
  }
  def getOutgoingFriendshipStringifiedIds(cursor: Long = -1): Future[UserStringifiedIds] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    getGenericOutgoingFriendships[UserStringifiedIds](parameters)
  }

  private def getGenericOutgoingFriendships[T: Manifest](parameters: FriendshipParameters): Future[T] =
    Get(s"$friendshipsUrl/outgoing.json", parameters).respondAs[T]

  def followUserId(user_id: Long, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(Some(user_id), screen_name = None, notify)
    genericFollow(parameters)
  }

  def followUser(screen_name: String, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(user_id = None, Some(screen_name), notify)
    genericFollow(parameters)
  }

  private def genericFollow(parameters: FollowParameters): Future[User] =
    Post(s"$friendshipsUrl/create.json", parameters).respondAs[User]

  def unfollowUser(screen_name: String): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(user_id = None, Some(screen_name))
    genericUnfollow(parameters)
  }

  def unfollowUserId(user_id: Long): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(Some(user_id), screen_name = None)
    genericUnfollow(parameters)
  }

  private def genericUnfollow(parameters: UnfollowParameters): Future[User] =
    Post(s"$friendshipsUrl/destroy.json", parameters).respondAs[User]
  
  def enableRetweetsNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = true)
    genericNotifications(parameters)
  }

  def enableRetweetsNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = true)
    genericNotifications(parameters)
  }

  def disableRetweetsNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = false)
    genericNotifications(parameters)
  }

  def disableRetweetsNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = false)
    genericNotifications(parameters)
  }

  def enableDeviceNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = true)
    genericNotifications(parameters)
  }

  def enableDeviceNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = true)
    genericNotifications(parameters)
  }

  def disableDeviceNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = false)
    genericNotifications(parameters)
  }

  def disableDeviceNotifications(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = false)
    genericNotifications(parameters)
  }

  private def genericNotifications(parameters: NotificationParameters): Future[Relationship] =
    Post(s"$friendshipsUrl/update.json", parameters).respondAs[Relationship]

  def getRelationshipBetweenUserIds(source_id: Long, target_id: Long): Future[Relationship] = {
    val parameters = RelationshipParametersByIds(source_id, target_id)
    genericGetRelationship(parameters)
  }

  def getRelationshipBetweenUsers(source_screen_name: String, target_screen_name: String): Future[Relationship] = {
    val parameters = RelationshipParametersByNames(source_screen_name, target_screen_name)
    genericGetRelationship(parameters)
  }

  private def genericGetRelationship(parameters: RelationshipParameters): Future[Relationship] =
    Get(s"$friendshipsUrl/show.json", parameters).respondAs[Relationship]

  def getRelationshipsWithUsers(screen_names: String*): Future[Seq[LookupRelationship]] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RelationshipsParameters(user_id = None, screen_name = Some(screen_names.mkString(",")))
    genericGetRelationships(parameters)
  }

  def getRelationshipsWithUserIds(user_ids: Long*): Future[Seq[LookupRelationship]] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = RelationshipsParameters(user_id = Some(user_ids.mkString(",")), screen_name = None)
    genericGetRelationships(parameters)
  }

  private def genericGetRelationships(parameters: RelationshipsParameters): Future[Seq[LookupRelationship]] =
    Get(s"$friendshipsUrl/lookup.json", parameters).respondAs[Seq[LookupRelationship]]
}
