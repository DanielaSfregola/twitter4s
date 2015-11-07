package com.danielasfregola.twitter4s.http.clients.directmessages

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.DirectMessage
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.directmessages.parameters._
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterDirectMessageClient extends OAuthClient with Configurations {

  private val directMessagesUrl = s"$apiTwitterUrl/$twitterVersion/direct_messages"

  def directMessage(id: Long): Future[DirectMessage] = {
    val parameters = ShowParameters(id)
    Get(s"$directMessagesUrl/show.json", parameters).respondAs[DirectMessage]
  }

  def createDirectMessage(user_id: Long,
                          text: String): Future[DirectMessage] = {
    val parameters = CreateParameters(user_id = Some(user_id),text = text)
    genericCreateDirectMessage(parameters)
  }

  def createDirectMessage(screen_name: String,
                          text: String): Future[DirectMessage] = {
    val parameters = CreateParameters(screen_name = Some(screen_name),text = text)
    genericCreateDirectMessage(parameters)
  }

  private def genericCreateDirectMessage(parameters: CreateParameters): Future[DirectMessage] =
    Get(s"$directMessagesUrl/new.json", parameters).respondAs[DirectMessage]

  def sentDirectMessages(since_id: Option[Long] = None,
                         max_id: Option[Long] = None,
                         count: Int = 200,
                         include_entities: Boolean = true,
                         page: Int = -1): Future[Seq[DirectMessage]] = {
    val parameters = SentParameters(since_id, max_id, count, include_entities, page)
    Get(s"$directMessagesUrl/sent.json", parameters).respondAs[Seq[DirectMessage]]
  }

  def receivedDirectMessages(since_id: Option[Long] = None,
                             max_id: Option[Long] = None,
                             count: Int = 200,
                             include_entities: Boolean = true,
                             skip_status: Boolean = false): Future[Seq[DirectMessage]] = {
    val parameters = ReceivedParameters(since_id, max_id, count, include_entities, skip_status)
    Get(s"$directMessagesUrl.json", parameters).respondAs[Seq[DirectMessage]]
  }

  def deleteDirectMessage(id: Long, include_entities: Boolean = true): Future[DirectMessage] = {
    val parameters = DestroyParameters(id, include_entities)
    Post(s"$directMessagesUrl/destroy.json", parameters).respondAs[DirectMessage]
  }
}
