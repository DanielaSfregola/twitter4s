package com.danielasfregola.twitter4s.http.clients.followers

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{Users, UserStringifiedIds, UserIds}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.followers.parameters.{FollowersParameters, FollowingParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `followers` resource.
  * */
trait TwitterFollowerClient extends OAuthClient with Configurations {

  private val followersUrl = s"$apiTwitterUrl/$twitterVersion/followers"

  def getFollowerIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFollowerIds[UserIds](parameters)
  }

  def getFollowerIdsForUser(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFollowerIds[UserIds](parameters)
  }

  def getFollowerStringifiedIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFollowerIds[UserStringifiedIds](parameters)
  }

  def getFollowersStringifiedIdsForUser(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFollowerIds[UserStringifiedIds](parameters)
  }

  private def genericFollowerIds[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$followersUrl/ids.json", parameters).respondAs[T]

  def getFollowersForUser(screen_name: String,
                          cursor: Long = -1,
                          count: Int = -1,
                          skip_status: Boolean = false,
                          include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FollowersParameters(user_id = None, screen_name = Some(screen_name), cursor, count, skip_status, include_user_entities)
    genericGetFollowers(parameters)
  }

  def getFollowersForUserId(user_id: Long,
                            cursor: Long = -1,
                            count: Int = -1,
                            skip_status: Boolean = false,
                            include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FollowersParameters(user_id = Some(user_id), screen_name = None, cursor, count, skip_status, include_user_entities)
    genericGetFollowers(parameters)
  }

  private def genericGetFollowers(parameters: FollowersParameters): Future[Users] =
    Get(s"$followersUrl/list.json", parameters).respondAs[Users]
}
