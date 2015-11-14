package com.danielasfregola.twitter4s.http.clients.friends

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.friends.parameters.{FriendsParameters, FriendParameters}
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterFriendClient extends OAuthClient with Configurations {

  private val friendsUrl = s"$apiTwitterUrl/$twitterVersion/friends"

  def friendsIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsIds(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFriends[UserIds](parameters)
  }

  def friendsForUserIdStringified(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FriendParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFriends[UserStringifiedIds](parameters)
  }

  def friendsStringified(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FriendParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFriends[UserStringifiedIds](parameters)
  }

  private def genericFriends[T: Manifest](parameters: FriendParameters): Future[T] =
    Get(s"$friendsUrl/ids.json", parameters).respondAs[T]


  def friends(screen_name: String,
              cursor: Long = -1,
              count: Int = 20,
              skip_status: Boolean = false,
              include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FriendsParameters(user_id = None, Some(screen_name), cursor, count, skip_status, include_user_entities)
    genericFriends(parameters)
  }

  def friendsForUserId(user_id: Long,
                       cursor: Long = -1,
                       count: Int = 20,
                       skip_status: Boolean = false,
                       include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FriendsParameters(Some(user_id), screen_name = None, cursor, count, skip_status, include_user_entities)
    genericFriends(parameters)
  }

  private def genericFriends(parameters: FriendsParameters): Future[Users] =
    Get(s"$friendsUrl/list.json", parameters).respondAs[Users]
}
