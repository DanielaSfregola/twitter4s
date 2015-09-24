package twitter4s.statuses

import scala.concurrent.Future

import twitter4s.entities.Tweet
import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterStatusClient extends OAuthClient with Configurations {

  val baseUrl = s"$twitterUrl/$apiVersion/statuses"

  def mentionsTimeline(count: Option[Int] = None,
               since_id: Option[Long] = None,
               max_id: Option[Long] = None,
               trim_user: Boolean = false,
               contributor_details: Boolean = false,
               include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = MentionsParameters(count, since_id, max_id, trim_user, contributor_details, include_entities)
    Get(s"$baseUrl/mentions_timeline.json?$parameters").respondAs[Seq[Tweet]]
  }

  def userTimeline(user_id: Option[Long] = None,
               screen_name: Option[String] = None,
               since_id: Option[Long] = None,
               count: Option[Int] = None,
               max_id: Option[Long] = None,
               trim_user: Boolean = false,
               exclude_replies: Boolean = false,
               contributor_details: Boolean = false,
               include_rts: Boolean = true
              ): Future[Seq[Tweet]] = {
    val parameters = TimelineParameters(user_id, screen_name, since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)
    Get(s"$baseUrl/user_timeline.json?$parameters").respondAs[Seq[Tweet]]
  }

}

