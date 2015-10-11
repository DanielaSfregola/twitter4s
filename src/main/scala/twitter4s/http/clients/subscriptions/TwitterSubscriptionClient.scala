package twitter4s.http.clients.subscriptions

import scala.concurrent.Future

import twitter4s.entities.{Tweet, Subscription}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.subscriptions.parameters.{RemoveMemberParameters, SubscriptionTimelineParameters, SubscribedListsParameters}
import twitter4s.util.Configurations

trait TwitterSubscriptionClient extends OAuthClient with Configurations {

  val subscriptionsUrl = s"$apiTwitterUrl/$twitterVersion/lists"

  def subscriptions(screen_name: String, reverse: Boolean = false): Future[Seq[Subscription]] = {
    val parameters = SubscribedListsParameters(user_id = None, Some(screen_name), reverse)
    genericSubscriptions(parameters)
  }

  def subscriptionsPerUserId(user_id: Long, reverse: Boolean = false): Future[Seq[Subscription]] = {
    val parameters = SubscribedListsParameters(Some(user_id), screen_name = None, reverse)
    genericSubscriptions(parameters)
  }

  private def genericSubscriptions(parameters: SubscribedListsParameters): Future[Seq[Subscription]] =
    Get(s"$subscriptionsUrl/list.json", parameters).respondAs[Seq[Subscription]]

  def subscriptionTimelinePerSlugAndOwnerId(slug: String,
                                            owner_id: Long,
                                            count: Int = 20,
                                            since_id: Option[Long] = None,
                                            max_id: Option[Long] = None,
                                            include_entities: Boolean = true,
                                            include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = SubscriptionTimelineParameters(
      slug = Some(slug), owner_id = Some(owner_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericSubscriptionTimeline(parameters)
  }

  def subscriptionTimelinePerSlugAndOwner(slug: String,
                                          owner_screen_name: String,
                                          count: Int = 20,
                                          since_id: Option[Long] = None,
                                          max_id: Option[Long] = None,
                                          include_entities: Boolean = true,
                                          include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = SubscriptionTimelineParameters(
      slug = Some(slug), owner_screen_name = Some(owner_screen_name), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericSubscriptionTimeline(parameters)
  }

  def subscriptionTimelinePerId(subscription_id: Long,
                                count: Int = 20,
                                since_id: Option[Long] = None,
                                max_id: Option[Long] = None,
                                include_entities: Boolean = true,
                                include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = SubscriptionTimelineParameters(
      list_id = Some(subscription_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericSubscriptionTimeline(parameters)
  }

  private def genericSubscriptionTimeline(parameters: SubscriptionTimelineParameters): Future[Seq[Tweet]] =
    Get(s"$subscriptionsUrl/statuses.json", parameters).respondAs[Seq[Tweet]]

  def removeMemberPerId(subscription_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(subscription_id), screen_name = Some(member_screen_name))
    genericRemoveMember(parameters)
  }

  def removeMemberPerSlugAndOwner(slug: String, owner_screen_name: String, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            screen_name = Some(member_screen_name))
    genericRemoveMember(parameters)
  }

  def removeMemberPerSlugAndOwnerId(slug: String, owner_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            screen_name = Some(member_screen_name))
    genericRemoveMember(parameters)
  }

  def removeMemberIdPerId(subscription_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(subscription_id), user_id = Some(member_id))
    genericRemoveMember(parameters)
  }

  def removeMemberIdPerSlugAndOwner(slug: String, owner_screen_name: String, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            user_id = Some(member_id))
    genericRemoveMember(parameters)
  }

  def removeMemberIdPerSlugAndOwnerId(slug: String, owner_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            user_id = Some(member_id))
    genericRemoveMember(parameters)
  }

  private def genericRemoveMember(parameters: RemoveMemberParameters): Future[Unit] =
    Post(s"$subscriptionsUrl/members/destroy.json", parameters).respondAs[Unit]

}
