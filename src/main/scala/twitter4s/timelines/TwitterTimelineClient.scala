package twitter4s.timelines

import twitter4s.http.clients.OAuthClient
import twitter4s.timelines.entities.MentionsOptions

trait TwitterTimelineClient {
  self: OAuthClient =>

  // TODO - how to deal with timelines?

  def mentions(count: Option[Int] = None,
               since_id: Option[Long] = None,
               max_id: Option[Long] = None,
               trim_user: Boolean = false,
               contributor_details: Boolean = false,
               include_entities: Boolean = true): AnyRef = {
    val options = MentionsOptions(count, since_id, max_id, trim_user, contributor_details, include_entities)
    mentions(options)
  }

  private def mentions(options: MentionsOptions): AnyRef = ???

}
