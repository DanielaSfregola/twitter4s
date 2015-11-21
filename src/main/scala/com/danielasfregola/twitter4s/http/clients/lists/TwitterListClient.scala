package com.danielasfregola.twitter4s.http.clients.lists

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.Mode
import com.danielasfregola.twitter4s.entities.enums.Mode.Mode
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.lists.parameters._
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `lists` resource.
  */
trait TwitterListClient extends OAuthClient with Configurations {

  private val listsUrl = s"$apiTwitterUrl/$twitterVersion/lists"

  /** Returns all lists the specified user subscribes to, including their own.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/list</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param reverse : By default it is `false`.
    *                Set this to true if you would like owned lists to be returned first.
    * @return : The sequence of all lists the specified user subscribes to.
    */
  def getListsForUser(screen_name: String, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(user_id = None, Some(screen_name), reverse)
    genericGetLists(parameters)
  }

  /** Returns all lists the specified user subscribes to, including their own.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/list</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param reverse : By default it is `false`.
    *                Set this to true if you would like owned lists to be returned first.
    * @return : The sequence of all lists the specified user subscribes to.
    */
  def getListsForUserId(user_id: Long, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(Some(user_id), screen_name = None, reverse)
    genericGetLists(parameters)
  }

  private def genericGetLists(parameters: ListsParameters): Future[Seq[TwitterList]] =
    Get(s"$listsUrl/list.json", parameters).respondAs[Seq[TwitterList]]

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit entities from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineBySlugAndOwnerId(slug: String,
                                      owner_id: Long,
                                      count: Int = 20,
                                      since_id: Option[Long] = None,
                                      max_id: Option[Long] = None,
                                      include_entities: Boolean = true,
                                      include_rts: Boolean = false): Future[Seq[Status]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_id = Some(owner_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericLGetListTimeline(parameters)
  }

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : TThe screen name of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit entities from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineBySlugAndOwnerName(slug: String,
                                        owner_screen_name: String,
                                        count: Int = 20,
                                        since_id: Option[Long] = None,
                                        max_id: Option[Long] = None,
                                        include_entities: Boolean = true,
                                        include_rts: Boolean = false): Future[Seq[Status]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_screen_name = Some(owner_screen_name), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericLGetListTimeline(parameters)
  }

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit entities from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineByListId(list_id: Long,
                              count: Int = 20,
                              since_id: Option[Long] = None,
                              max_id: Option[Long] = None,
                              include_entities: Boolean = true,
                              include_rts: Boolean = false): Future[Seq[Status]] = {
    val parameters = ListTimelineParameters(
      list_id = Some(list_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericLGetListTimeline(parameters)
  }

  private def genericLGetListTimeline(parameters: ListTimelineParameters): Future[Seq[Status]] =
    Get(s"$listsUrl/statuses.json", parameters).respondAs[Seq[Status]]

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeMemberFromListByListId(list_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeMemberFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeMemberFromListBySlugAndOwnerId(slug: String, owner_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            screen_name = Some(member_screen_name))
    genericRemoveMemberFromList(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeMemberIdFromListByListId(list_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeMemberIdFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeMemberIdFromListBySlugAndOwnerId(slug: String, owner_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            user_id = Some(member_id))
    genericRemoveMemberFromList(parameters)
  }

  private def genericRemoveMemberFromList(parameters: RemoveMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy.json", parameters).respondAs[Unit]

  /** Returns the twitter lists the specified user has been added to.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/memberships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/memberships</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param filter_to_owned_lists : By default it is `false`.
    *                    When set to `true`, will return just lists the authenticating user owns, and the user represented by user_id or screen_name is a member of.
    * @return : The twitter lists the specified user has been added to.
    */
  def getMembershipsForUser(screen_name: String,
                            count: Int = 20,
                            cursor: Long = -1,
                            filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(user_id = None, Some(screen_name), count, cursor, filter_to_owned_lists)
    genericGetMemberships(parameters)
  }

  /** Returns the twitter lists the specified user has been added to.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/memberships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/memberships</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param filter_to_owned_lists : By default it is `false`.
    *                    When set to `true`, will return just lists the authenticating user owns, and the user represented by user_id or screen_name is a member of.
    * @return : The twitter lists the specified user has been added to.
    */
  def getMembershipsForUserId(user_id: Long,
                              count: Int = 20,
                              cursor: Long = -1,
                              filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(Some(user_id), screen_name = None, count, cursor, filter_to_owned_lists)
    genericGetMemberships(parameters)
  }

  private def genericGetMemberships(parameters: MembershipsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/memberships.json", parameters).respondAs[TwitterLists]

  def addMemberIdsToListByListId(list_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(list_id = Some(list_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMemberIdsToListBySlugAndOwnerName(slug: String, owner_screen_name: String, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMemberIdsToListBySlugAndOwnerId(slug: String, owner_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_ids.mkString(",")))
    genericAddMembersToList(parameters)
  }

  def addMembersToListByListId(list_id: Long, screen_names: Seq[String]): Future[Unit] = {
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

  def getMemberByUserIdAndListId(list_id: Long,
                                 user_id: Long,
                                 include_entities: Boolean = true,
                                 skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  def getMemberByUserIdSlugAndOwnerName(slug: String,
                                        owner_screen_name: String,
                                        user_id: Long,
                                        include_entities: Boolean = true,
                                        skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  def getMemberByUserIdSlugAndOwnerId(slug: String,
                                      owner_id: Long,
                                      user_id: Long,
                                      include_entities: Boolean = true,
                                      skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  def getMemberByUserAndListId(list_id: Long,
                               screen_name: String,
                               include_entities: Boolean = true,
                               skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  def getMemberByUserSlugAndOwnerName(slug: String,
                                      owner_screen_name: String,
                                      screen_name: String,
                                      include_entities: Boolean = true,
                                      skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  def getMemberByUserSlugAndOwnerId(slug: String,
                                    owner_id: Long,
                                    screen_name: String,
                                    include_entities: Boolean = true,
                                    skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericGetMember(parameters)
  }

  private def genericGetMember(parameters: MemberParameters): Future[User] =
    Get(s"$listsUrl/members/show.json", parameters).respondAs[User]

  def listMembers(list_id: Long,
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

  def addMemberIdToList(list_id: Long, user_id: Long): Future[Unit] = {
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

  def addMemberToList(list_id: Long, screen_name: String): Future[Unit] = {
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

  def deleteList(list_id: Long): Future[TwitterList] = {
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

  def updateListMode(list_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListModeBySlugAndOwnerName(slug: String, owner_screen_name: String, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListModeBySlugAndOwnerId(slug: String, owner_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  def updateListName(list_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListNameBySlugAndOwnerName(slug: String, owner_screen_name: String, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListNameBySlugAndOwnerId(slug: String, owner_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  def updateListDescription(list_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  def updateListDescriptionBySlugAndOwnerName(slug: String, owner_screen_name: String, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  def updateListDescriptionBySlugAndOwnerId(slug: String, owner_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }
  
  def updateList(list_id: Long, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericUpdateList(parameters, update)
  }
  
  def updateListBySlugAndOwnerName(slug: String, owner_screen_name: String, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericUpdateList(parameters, update)
  }

  def updateListBySlugAndOwnerId(slug: String, owner_id: Long, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericUpdateList(parameters, update)
  }

  private def genericUpdateList(parameters: ListParameters, update: TwitterListUpdate): Future[Unit] = {
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
    val parameters = CreateListParameters(name, mode, description)
    Post(s"$listsUrl/create.json", parameters).respondAs[TwitterList]
  }

  def list(list_id: Long): Future[TwitterList] = {
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

  def listSubscriptions(screen_name: String,
                        count: Int = 20,
                        cursor: Long = -1): Future[TwitterLists] = {
    val parameters = SubscriptionsParameters(user_id = None, Some(screen_name), count, cursor)
    genericListSubscriptions(parameters)
  }

  def listSubscriptionsByUserId(user_id: Long,
                                count: Int = 20,
                                cursor: Long = -1): Future[TwitterLists] = {
    val parameters = SubscriptionsParameters(Some(user_id), screen_name = None, count, cursor)
    genericListSubscriptions(parameters)
  }

  private def genericListSubscriptions(parameters: SubscriptionsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/subscriptions.json", parameters).respondAs[TwitterLists]

  def removeMembersFromList(list_id: Long, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(list_id = Some(list_id), screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  def removeMembersFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_screen_name = Some(owner_screen_name),
      screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  def removeMembersFromListBySlugAndOwnerId(slug: String, owner_id: Long, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_id = Some(owner_id),
      screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  def removeMembersIdsFromList(list_id: Long, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(list_id = Some(list_id), user_id = Some(members_ids.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  def removeMembersIdsFromListBySlugAndOwnerName(slug: String, owner_screen_name: String, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_screen_name = Some(owner_screen_name),
      user_id = Some(members_ids.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  def removeMembersIdsFromListBySlugAndOwnerId(slug: String, owner_id: Long, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_id = Some(owner_id),
      user_id = Some(members_ids.mkString(",")))
    genericRemoveMembersFromList(parameters)
  }

  private def genericRemoveMembersFromList(parameters: RemoveMembersParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy_all.json", parameters).respondAs[Unit]

  def ownerships(screen_name: String, count: Int = 20, cursor: Long = -1): Future[TwitterLists] = {
    val parameters = OwnershipsParameters(user_id = None, Some(screen_name), count, cursor)
    genericOwnerships(parameters)
  }

  def ownershipsForUserId(user_id: Long, count: Int = 20, cursor: Long = -1): Future[TwitterLists] = {
    val parameters = OwnershipsParameters(Some(user_id), screen_name = None, count, cursor)
    genericOwnerships(parameters)
  }

  private def genericOwnerships(parameters: OwnershipsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/ownerships.json", parameters).respondAs[TwitterLists]
}
