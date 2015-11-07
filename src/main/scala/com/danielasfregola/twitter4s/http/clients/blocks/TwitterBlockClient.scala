package com.danielasfregola.twitter4s.http.clients.blocks

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{User, Users}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.blocks.parameters.{BlockParameters, BlockedUsersParameters}
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterBlockClient extends OAuthClient with Configurations {

  private val blocksUrl = s"$apiTwitterUrl/$twitterVersion/blocks"

  def blockedUsers(include_entities: Boolean = true,
                   skip_status: Boolean = false,
                   cursor: Long = -1): Future[Users] = {
    val parameters = BlockedUsersParameters(include_entities, skip_status, cursor)
    Get(s"$blocksUrl/list.json", parameters).respondAs[Users]
  }

  def block(screen_name: String,
            include_entities: Boolean = true,
            skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericBlock(parameters)
  }

  def blockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericBlock(parameters)
  }

  private def genericBlock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/create.json", parameters).respondAs[User]

  def unblock(screen_name: String,
            include_entities: Boolean = true,
            skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(user_id = None, Some(screen_name), include_entities, skip_status)
    genericUnblock(parameters)
  }

  def unblockUserId(user_id: Long,
                  include_entities: Boolean = true,
                  skip_status: Boolean = false): Future[User] = {
    val parameters = BlockParameters(Some(user_id), screen_name = None, include_entities, skip_status)
    genericUnblock(parameters)
  }

  private def genericUnblock(parameters: BlockParameters): Future[User] =
    Post(s"$blocksUrl/destroy.json", parameters).respondAs[User]
}
