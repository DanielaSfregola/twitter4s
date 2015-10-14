package twitter4s.http.clients.lists

import scala.concurrent.Future

import twitter4s.entities._
import twitter4s.entities.enums.Mode
import twitter4s.entities.enums.Mode.Mode
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.lists.parameters._
import twitter4s.util.Configurations

trait TwitterListClient extends OAuthClient with Configurations {

  val listsUrl = s"$apiTwitterUrl/$twitterVersion/lists"

  def listsByScreenName(screen_name: String, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(user_id = None, Some(screen_name), reverse)
    genericLists(parameters)
  }

  def listsByUserId(user_id: Long, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(Some(user_id), screen_name = None, reverse)
    genericLists(parameters)
  }

  private def genericLists(parameters: ListsParameters): Future[Seq[TwitterList]] =
    Get(s"$listsUrl/list.json", parameters).respondAs[Seq[TwitterList]]

  def listTimelineBySlugAndOwnerId(slug: String,
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

  def listTimelineBySlugAndOwnerName(slug: String,
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

  def listTimelineById(list_id: Long,
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

  def removeMemberFromListById(list_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  def removeMemberFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  def removeMemberFromListBySlugAndOwnerId(slug: String, owner_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  def removeMemberIdFromListById(list_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  def removeMemberIdFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  def removeMemberIdFromListBySlugAndOwnerId(slug: String, owner_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  private def genericRemoveMemberFromList(parameters: RemoveMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy.json", parameters).respondAs[Unit]

  def membershipsByScreenName(screen_name: String,
                  count: Int = 20,
                  cursor: Long = -1,
                  filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(user_id = None, Some(screen_name), count, cursor, filter_to_owned_lists)
    genericMemberships(parameters)
  }

  def membershipsByUserId(user_id: Long,
                           count: Int = 20,
                           cursor: Long = -1,
                           filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(Some(user_id), screen_name = None, count, cursor, filter_to_owned_lists)
    genericMemberships(parameters)
  }

  private def genericMemberships(parameters: MembershipsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/memberships.json", parameters).respondAs[TwitterLists]

  def addMembersIdsToListById(list_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(list_id = Some(list_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersIdsToListBySlugAndOwnerName(slug: String, owner_screen_name: String, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersIdsToListBySlugAndOwnerId(slug: String, owner_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersToListById(list_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(list_id = Some(list_id), screen_name = Some(screen_names.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersToListBySlugAndOwnerName(slug: String, owner_screen_name: String, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), screen_name = Some(screen_names.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersToListBySlugAndOwnerId(slug: String, owner_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), screen_name = Some(screen_names.mkString(",")))
    genericAddMembersToList(parameters)
  }

  private def genericAddMembersToList(parameters: MembersParameters): Future[Unit] =
    Post(s"$listsUrl/members/create_all.json", parameters).respondAs[Unit]

  def listMemberIdById(list_id: Long,
                          user_id: Long,
                          include_entities: Boolean = true,
                          skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  def listMemberIdBySlugAndOwnerName(slug: String,
                               owner_screen_name: String,
                               user_id: Long,
                               include_entities: Boolean = true,
                               skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  def listMemberIdBySlugAndOwnerId(slug: String,
                                  owner_id: Long,
                                  user_id: Long,
                                  include_entities: Boolean = true,
                                  skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  def listMemberById(list_id: Long,
                      screen_name: String,
                      include_entities: Boolean = true,
                      skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  def listMemberBySlugAndOwnerName(slug: String,
                            owner_screen_name: String,
                            screen_name: String,
                            include_entities: Boolean = true,
                            skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  def listMemberBySlugAndOwnerId(slug: String,
                              owner_id: Long,
                              screen_name: String,
                              include_entities: Boolean = true,
                              skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericListMember(parameters)
  }

  private def genericListMember(parameters: MemberParameters): Future[User] =
    Get(s"$listsUrl/members/show.json", parameters).respondAs[User]

  def listMembersById(list_id: Long,
                    count: Int = 20,
                    cursor: Long = -1,
                    include_entities: Boolean = true,
                    skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(list_id = Some(list_id),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericListMembers(parameters)
  }

  def listMembersBySlugAndOwnerName(slug: String,
                                   owner_screen_name: String,
                                   count: Int = 20,
                                   cursor: Long = -1,
                                   include_entities: Boolean = true,
                                   skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(slug = Some(slug),
                                             owner_screen_name = Some(owner_screen_name),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericListMembers(parameters)
  }

  def listMembersBySlugAndOwnerId(slug: String,
                                     owner_id: Long,
                                     count: Int = 20,
                                     cursor: Long = -1,
                                     include_entities: Boolean = true,
                                     skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(slug = Some(slug),
                                             owner_id = Some(owner_id),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericListMembers(parameters)
  }

  private def genericListMembers(parameters: ListMembersParameters): Future[Users] =
    Get(s"$listsUrl/members.json", parameters).respondAs[Users]

  def addMemberIdToListById(list_id: Long, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(list_id = Some(list_id), user_id = Some(user_id))
    genericAddMemberToList(parameters)
  }

  def addMemberIdToListBySlugAndOwnerName(slug: String, owner_screen_name: String, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_id))
    genericAddMemberToList(parameters)
  }

  def addMemberIdToListBySlugAndOwnerId(slug: String, owner_id: Long, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_id))
    genericAddMemberToList(parameters)
  }

  def addMemberToListById(list_id: Long, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(list_id = Some(list_id), screen_name = Some(screen_name))
    genericAddMemberToList(parameters)
  }

  def addMemberToListBySlugAndOwnerName(slug: String, owner_screen_name: String, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), screen_name = Some(screen_name))
    genericAddMemberToList(parameters)
  }

  def addMemberToListBySlugAndOwnerId(slug: String, owner_id: Long, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_id = Some(owner_id), screen_name = Some(screen_name))
    genericAddMemberToList(parameters)
  }

  private def genericAddMemberToList(parameters: AddMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/create.json", parameters).respondAs[Unit]

  def deleteListById(list_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericDeleteList(parameters)
  }

  def deleteListBySlugAndOwnerName(slug: String, owner_screen_name: String): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericDeleteList(parameters)
  }

  def deleteListBySlugAndOwnerId(slug: String, owner_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericDeleteList(parameters)
  }

  private def genericDeleteList(parameters: ListParameters): Future[TwitterList] =
    Post(s"$listsUrl/destroy.json", parameters).respondAs[TwitterList]

  def updateListModeById(list_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = ListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListModeBySlugAndOwnerName(slug: String, owner_screen_name: String, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = ListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListModeBySlugAndOwnerId(slug: String, owner_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = ListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListNameById(list_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = ListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListNameBySlugAndOwnerName(slug: String, owner_screen_name: String, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = ListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListNameBySlugAndOwnerId(slug: String, owner_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = ListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListDescriptionById(list_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = ListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  def updateListDescriptionBySlugAndOwnerName(slug: String, owner_screen_name: String, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = ListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  def updateListDescriptionBySlugAndOwnerId(slug: String, owner_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = ListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }
  
  def updateListById(list_id: Long, update: ListUpdate): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericUpdateList(parameters, update)
  }
  
  def updateListBySlugAndOwnerName(slug: String, owner_screen_name: String, update: ListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericUpdateList(parameters, update)
  }

  def updateListBySlugAndOwnerId(slug: String, owner_id: Long, update: ListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericUpdateList(parameters, update)
  }

  private def genericUpdateList(parameters: ListParameters, update: ListUpdate): Future[Unit] = {
    val listUpdate = UpdateListParameters(
      list_id = parameters.list_id,
      slug = parameters.slug,
      owner_screen_name = parameters.owner_screen_name,
      owner_id = parameters.owner_id,
      description = update.description,
      name = update.name,
      mode = update.mode)
    Post(s"$listsUrl/update.json", listUpdate).respondAs[Unit]
  }

  def createList(name: String, mode: Mode = Mode.Public, description: Option[String] = None): Future[TwitterList] = {
    val parameteres = CreateListParameters(name, mode, description)
    Post(s"$listsUrl/create.json", parameteres).respondAs[TwitterList]
  }

  def listById(list_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericList(parameters)
  }

  def listBySlugAndOwnerName(slug: String, owner_screen_name: String): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericList(parameters)
  }

  def listBySlugAndOwnerId(slug: String, owner_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericList(parameters)
  }

  private def genericList(parameters: ListParameters): Future[TwitterList] =
    Get(s"$listsUrl/show.json", parameters).respondAs[TwitterList]
}
