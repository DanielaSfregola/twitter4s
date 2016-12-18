package com.danielasfregola.twitter4s.http.clients.rest.directmessages

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.DirectMessage
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.rest.directmessages.parameters._
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `direct_messages` resource.
  * */
trait TwitterDirectMessageClient extends OAuthClient with Configurations {

  private val directMessagesUrl = s"$apiTwitterUrl/$twitterVersion/direct_messages"

  /** Returns a single direct message, specified by an id parameter.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/direct_messages/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/direct_messages/show</a>.
    *
    * @param id : The ID of the direct message.
    * @return : The direct message.
    * */
  def directMessage(id: Long): Future[DirectMessage] = {
    val parameters = ShowParameters(id)
    Get(s"$directMessagesUrl/show.json", parameters).respondAs[DirectMessage]
  }

  @deprecated("use directMessage instead", "2.2")
  def getDirectMessage(id: Long): Future[DirectMessage] =
    directMessage(id)

  /** Sends a new direct message to the specified user from the authenticating user.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/direct_messages/new" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/direct_messages/new</a>.
    *
    * @param user_id : The ID of the user who should receive the direct message.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param text : The text of your direct message. Be sure to URL encode as necessary,
    *             and keep the message under 140 characters.
    * @return : The sent direct message.
    * */
  def createDirectMessage(user_id: Long,
                          text: String): Future[DirectMessage] = {
    val parameters = CreateParameters(user_id = Some(user_id),text = text)
    genericCreateDirectMessage(parameters)
  }

  /** Sends a new direct message to the specified user from the authenticating user.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/direct_messages/new" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/direct_messages/new</a>.
    *
    * @param screen_name : The screen name of the user who should receive the direct message.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param text : The text of your direct message. Be sure to URL encode as necessary,
    *             and keep the message under 140 characters.
    * @return : The sent direct message.
    * */
  def createDirectMessage(screen_name: String,
                          text: String): Future[DirectMessage] = {
    val parameters = CreateParameters(screen_name = Some(screen_name),text = text)
    genericCreateDirectMessage(parameters)
  }

  private def genericCreateDirectMessage(parameters: CreateParameters): Future[DirectMessage] =
    Get(s"$directMessagesUrl/new.json", parameters).respondAs[DirectMessage]

  /** Returns the 20 most recent direct messages sent by the authenticating user.
    * Includes detailed information about the sender and recipient user.
    * You can request up to 200 direct messages per call, up to a maximum of 800 outgoing DMs.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/direct_messages/sent" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/direct_messages/sent</a>.
    *
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is 200.
    *              Specifies the number of records to retrieve. Must be less than or equal to 200.
    * @return : The sequence of sent direct messages.
    * */
  def sentDirectMessages(since_id: Option[Long] = None,
                         max_id: Option[Long] = None,
                         count: Int = 200,
                         include_entities: Boolean = true,
                         page: Int = -1): Future[Seq[DirectMessage]] = {
    val parameters = SentParameters(since_id, max_id, count, include_entities, page)
    Get(s"$directMessagesUrl/sent.json", parameters).respondAs[Seq[DirectMessage]]
  }

  def getSentDirectMessages(since_id: Option[Long] = None,
                            max_id: Option[Long] = None,
                            count: Int = 200,
                            include_entities: Boolean = true,
                            page: Int = -1): Future[Seq[DirectMessage]] =
    sentDirectMessages(since_id, max_id, count, include_entities, page)

  /** Returns the 20 most recent direct messages sent to the authenticating user.
    * Includes detailed information about the sender and recipient user.
    * You can request up to 200 direct messages per call, and only the most recent 200 DMs will be available using this endpoint.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/direct_messages" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/direct_messages</a>.
    *
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param count : By default it is 200.
    *              Specifies the number of direct messages to try and retrieve, up to a maximum of 200.
    *              The value of count is best thought of as a limit to the number of Tweets to return
    *              because suspended or deleted content is removed after the count has been applied.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true` statuses will not be included in the returned user object.
    * @return : The sequence of received direct messages.
    * */
  def receivedDirectMessages(since_id: Option[Long] = None,
                             max_id: Option[Long] = None,
                             count: Int = 200,
                             include_entities: Boolean = true,
                             skip_status: Boolean = false): Future[Seq[DirectMessage]] = {
    val parameters = ReceivedParameters(since_id, max_id, count, include_entities, skip_status)
    Get(s"$directMessagesUrl.json", parameters).respondAs[Seq[DirectMessage]]
  }

  @deprecated("use receivedDirectMessages instead", "2.2")
  def getReceivedDirectMessages(since_id: Option[Long] = None,
                                max_id: Option[Long] = None,
                                count: Int = 200,
                                include_entities: Boolean = true,
                                skip_status: Boolean = false): Future[Seq[DirectMessage]] =
    receivedDirectMessages(since_id, max_id, count, include_entities, skip_status)

  /** Destroys the direct message specified in the required ID parameter.
    * The authenticating user must be the recipient of the specified direct message.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/direct_messages/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/direct_messages/destroy</a>.
    *
    * @param id : The ID of the direct message to delete.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will not be included when set to false.
    * @return : The deleted direct message.
    * */
  def deleteDirectMessage(id: Long, include_entities: Boolean = true): Future[DirectMessage] = {
    val parameters = DestroyParameters(id, include_entities)
    Post(s"$directMessagesUrl/destroy.json", parameters).respondAs[DirectMessage]
  }
}
