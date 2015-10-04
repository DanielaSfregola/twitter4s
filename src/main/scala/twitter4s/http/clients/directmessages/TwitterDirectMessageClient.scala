package twitter4s.http.clients.directmessages

import scala.concurrent.Future

import twitter4s.entities.DirectMessage
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.directmessages.parameters.SentParameters
import twitter4s.util.Configurations

trait TwitterDirectMessageClient extends OAuthClient with Configurations {

  val directMessagesUrl = s"$apiTwitterUrl/$twitterVersion/direct_messages"

  def directMessagesSent(since_id: Option[Long] = None,
                         max_id: Option[Long] = None,
                         count: Int = 200,
                         include_entities: Boolean = true,
                         page: Int = -1): Future[Seq[DirectMessage]] = {
    val parameters = SentParameters(since_id, max_id, count, include_entities, page)
    Get(s"$directMessagesUrl/sent.json?$parameters").respondAs[Seq[DirectMessage]]
  }
}
