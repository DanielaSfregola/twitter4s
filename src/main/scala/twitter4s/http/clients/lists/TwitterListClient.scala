package twitter4s.http.clients.lists

import scala.concurrent.Future

import twitter4s.entities.{Subscription, Subscriptions, Tweet}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.lists.parameters._
import twitter4s.util.Configurations

trait TwitterListClient extends OAuthClient with Configurations {

  val listsUrl = s"$apiTwitterUrl/$twitterVersion/lists"

  def lists(screen_name: String, reverse: Boolean = false): Future[Seq[Subscription]] = {
    val parameters = ListsParameters(user_id = None, Some(screen_name), reverse)
    genericLists(parameters)
  }

  def listsPerUserId(user_id: Long, reverse: Boolean = false): Future[Seq[Subscription]] = {
    val parameters = ListsParameters(Some(user_id), screen_name = None, reverse)
    genericLists(parameters)
  }

  private def genericLists(parameters: ListsParameters): Future[Seq[Subscription]] =
    Get(s"$listsUrl/list.json", parameters).respondAs[Seq[Subscription]]

  def listTimelinePerSlugAndOwnerId(slug: String,
                                    owner_id: Long,
                                    count: Int = 20,
                                    since_id: Option[Long] = None,
                                    max_id: Option[Long] = None,
                                    include_entities: Boolean = true,
                                    include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_id = Some(owner_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericListTimeline(parameters)
  }

  def listTimelinePerSlugAndOwner(slug: String,
                                  owner_screen_name: String,
                                  count: Int = 20,
                                  since_id: Option[Long] = None,
                                  max_id: Option[Long] = None,
                                  include_entities: Boolean = true,
                                  include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_screen_name = Some(owner_screen_name), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericListTimeline(parameters)
  }

  def listTimelinePerListId(list_id: Long,
                        count: Int = 20,
                        since_id: Option[Long] = None,
                        max_id: Option[Long] = None,
                        include_entities: Boolean = true,
                        include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      list_id = Some(list_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericListTimeline(parameters)
  }

  private def genericListTimeline(parameters: ListTimelineParameters): Future[Seq[Tweet]] =
    Get(s"$listsUrl/statuses.json", parameters).respondAs[Seq[Tweet]]

  def removeListMemberPerListId(list_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), screen_name = Some(member_screen_name))
    genericListRemoveMember(parameters)
  }

  def removeListMemberPerSlugAndOwner(slug: String, owner_screen_name: String, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            screen_name = Some(member_screen_name))
    genericListRemoveMember(parameters)
  }

  def removeListMemberPerSlugAndOwnerId(slug: String, owner_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            screen_name = Some(member_screen_name))
    genericListRemoveMember(parameters)
  }

  def removeListMemberIdPerListId(list_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), user_id = Some(member_id))
    genericListRemoveMember(parameters)
  }

  def removeListMemberIdPerSlugAndOwner(slug: String, owner_screen_name: String, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            user_id = Some(member_id))
    genericListRemoveMember(parameters)
  }

  def removeListMemberIdPerSlugAndOwnerId(slug: String, owner_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            user_id = Some(member_id))
    genericListRemoveMember(parameters)
  }

  private def genericListRemoveMember(parameters: RemoveMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy.json", parameters).respondAs[Unit]

  def memberships(screen_name: String,
                  count: Int = 20,
                  cursor: Long = -1,
                  filter_to_owned_lists: Boolean = false): Future[Subscriptions] = {
    val parameters = MembershipsParameters(user_id = None, Some(screen_name), count, cursor, filter_to_owned_lists)
    genericMemberships(parameters)
  }

  def membershipsPerUserId(user_id: Long,
                           count: Int = 20,
                           cursor: Long = -1,
                           filter_to_owned_lists: Boolean = false): Future[Subscriptions] = {
    val parameters = MembershipsParameters(Some(user_id), screen_name = None, count, cursor, filter_to_owned_lists)
    genericMemberships(parameters)
  }

  private def genericMemberships(parameters: MembershipsParameters): Future[Subscriptions] =
    Get(s"$listsUrl/memberships.json", parameters).respondAs[Subscriptions]

  def addMembersIdsPerListId(list_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(list_id = Some(list_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembers(parameters)
  }

  def addMembersIdsPerSlugAndOwner(slug: String, owner_screen_name: String, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_ids.mkString(",")))
    genericAddMembers(parameters)
  }

  def addMembersIdsPerSlugAndOwnerId(slug: String, owner_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembers(parameters)
  }

  def addMembersPerListId(list_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(list_id = Some(list_id), screen_name = Some(screen_names.mkString(",")))
    genericAddMembers(parameters)
  }

  def addMembersPerSlugAndOwner(slug: String, owner_screen_name: String, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), screen_name = Some(screen_names.mkString(",")))
    genericAddMembers(parameters)
  }

  def addMembersPerSlugAndOwnerId(slug: String, owner_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), screen_name = Some(screen_names.mkString(",")))
    genericAddMembers(parameters)
  }

  private def genericAddMembers(parameters: MembersParameters): Future[Unit] =
    Post(s"$listsUrl/members/create_all.json", parameters).respondAs[Unit]

}
