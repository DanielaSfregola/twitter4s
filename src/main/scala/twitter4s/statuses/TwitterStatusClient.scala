package twitter4s.statuses

import scala.concurrent.Future

import twitter4s.entities.Status
import twitter4s.http.clients.OAuthClient
import twitter4s.utils.Configurations

trait TwitterStatusClient extends Configurations {
  self: OAuthClient =>

  val baseUrl = s"$twitterUrl/$apiVersion/statuses"

  def mentions(count: Option[Int] = None,
               since_id: Option[Long] = None,
               max_id: Option[Long] = None,
               trim_user: Boolean = false,
               contributor_details: Boolean = false,
               include_entities: Boolean = true): Future[List[Status]] = {
    val options = MentionsOptions(count, since_id, max_id, trim_user, contributor_details, include_entities)
    toResponse(Get(s"$baseUrl/mentions_timeline.json", options))
  }

  case class MentionsOptions(count: Option[Int] = None,
                             since_id: Option[Long] = None,
                             max_id: Option[Long] = None,
                             trim_user: Boolean = false,
                             contributor_details: Boolean = false,
                             include_entities: Boolean = true)

}

