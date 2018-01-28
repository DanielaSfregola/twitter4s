package com.danielasfregola.twitter4s.http.clients.rest.friendships

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.friendships.parameters._
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `friendships` resource.
  * */
trait TwitterFriendshipClient {

  protected val restClient: RestClient

  private val friendshipsUrl = s"$apiTwitterUrl/$twitterVersion/friendships"

  /** Returns a collection of user ids that the currently authenticated user does not want to receive retweets from.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-no_retweets-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-no_retweets-ids</a>.
    *
    * @return : The sequence of user ids the currently authenticated user does not want to receive retweets from.
    * */
  def noRetweetsUserIds(): Future[RatedData[Seq[Long]]] = {
    val parameters = BlockedParameters(stringify_ids = false)
    genericGetNoRetweetsUserIds[Seq[Long]](parameters)
  }

  /** Returns a collection of user stringified ids that the currently authenticated user does not want to receive retweets from.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-no_retweets-ids" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-no_retweets-ids</a>.
    *
    * @return : The sequence of the user stringified ids the currently authenticated user does not want to receive retweets from.
    * */
  def noRetweetsUserStringifiedIds(): Future[RatedData[Seq[String]]] = {
    val parameters = BlockedParameters(stringify_ids = true)
    genericGetNoRetweetsUserIds[Seq[String]](parameters)
  }

  private def genericGetNoRetweetsUserIds[T: Manifest](parameters: BlockedParameters): Future[RatedData[T]] = {
    import restClient._
    Get(s"$friendshipsUrl/no_retweets/ids.json", parameters).respondAsRated[T]
  }

  /** Returns a collection of numeric IDs for every user who has a pending request to follow the authenticating user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-incoming" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-incoming</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return : The sequence of the user ids that have a pending request to follow the authenticating user.
    * */
  def incomingFriendshipIds(cursor: Long = -1): Future[RatedData[UserIds]] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    genericGetIncomingFriendships[UserIds](parameters)
  }

  /** Returns a collection of numeric stringified IDs for every user who has a pending request to follow the authenticating user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-incoming" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-incoming</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return :  The sequence of the user stringified ids that have a pending request to follow the authenticating user.
    * */
  def incomingFriendshipStringifiedIds(cursor: Long = -1): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    genericGetIncomingFriendships[UserStringifiedIds](parameters)
  }

  private def genericGetIncomingFriendships[T: Manifest](parameters: FriendshipParameters): Future[RatedData[T]] = {
    import restClient._
    Get(s"$friendshipsUrl/incoming.json", parameters).respondAsRated[T]
  }

  /** Returns a collection of numeric IDs for every protected user for whom the authenticating user has a pending follow request.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-outgoing" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-outgoing</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return :  The sequence of the user ids that have a pending follow request from the authenticating user.
    * */
  def outgoingFriendshipIds(cursor: Long = -1): Future[RatedData[UserIds]] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = false)
    genericOutgoingFriendships[UserIds](parameters)
  }

  /** Returns a collection of numeric stringified IDs for every protected user for whom the authenticating user has a pending follow request.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-outgoing" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-outgoing</a>.
    *
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time.
    *               The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried.
    * @return :  The sequence of the user stringified ids that have a pending follow request from the authenticating user.
    * */
  def outgoingFriendshipStringifiedIds(cursor: Long = -1): Future[RatedData[UserStringifiedIds]] = {
    val parameters = FriendshipParameters(cursor, stringify_ids = true)
    genericOutgoingFriendships[UserStringifiedIds](parameters)
  }

  private def genericOutgoingFriendships[T: Manifest](parameters: FriendshipParameters): Future[RatedData[T]] = {
    import restClient._
    Get(s"$friendshipsUrl/outgoing.json", parameters).respondAsRated[T]
  }

  /** Allows the authenticating users to follow the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-create" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-create</a>.
    *
    * @param user_id : The ID of the user for whom to befriend.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param notify : By default it is `true`.
    *               Enable notifications for the target user.
    * @return :  The user representation of the target user.
    * */
  def followUserId(user_id: Long, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(Some(user_id), screen_name = None, notify)
    genericFollow(parameters)
  }

  /** Allows the authenticating users to follow the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-create" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-create</a>.
    *
    * @param screen_name : The screen name of the user for whom to befriend.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param notify : By default it is `true`.
    *               Enable notifications for the target user.
    * @return :  The user representation of the target user.
    * */
  def followUser(screen_name: String, notify: Boolean = true): Future[User] = {
    val parameters = FollowParameters(user_id = None, Some(screen_name), notify)
    genericFollow(parameters)
  }

  private def genericFollow(parameters: FollowParameters): Future[User] = {
    import restClient._
    Post(s"$friendshipsUrl/create.json", parameters).respondAs[User]
  }

  /** Allows the authenticating users to unfollow the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-destroy" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-destroy</a>.
    *
    * @param user_id : The ID of the user for whom to unfollow.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def unfollowUserId(user_id: Long): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(Some(user_id), screen_name = None)
    genericUnfollow(parameters)
  }

  /** Allows the authenticating users to unfollow the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-destroy" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-destroy</a>.
    *
    * @param screen_name : The screen name of the user for whom to unfollow.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def unfollowUser(screen_name: String): Future[User] = {
    val parameters: UnfollowParameters = UnfollowParameters(user_id = None, Some(screen_name))
    genericUnfollow(parameters)
  }

  private def genericUnfollow(parameters: UnfollowParameters): Future[User] = {
    import restClient._
    Post(s"$friendshipsUrl/destroy.json", parameters).respondAs[User]
  }

  /** Allows one to enable retweets from the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param screen_name : The screen name of the user for whom to befriend.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def enableRetweetsNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = true)
    genericNotifications(parameters)
  }

  /** Allows one to enable retweets from the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param user_id : The ID of the user for whom to befriend.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def enableRetweetsNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = true)
    genericNotifications(parameters)
  }

  /** Allows one to disable retweets from the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param screen_name : The screen name of the user for whom to befriend.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def disableRetweetsNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(user_id = None, Some(screen_name), retweets = false)
    genericNotifications(parameters)
  }

  /** Allows one to disable retweets from the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param user_id : The ID of the user for whom to befriend.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def disableRetweetsNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = RetweetNotificationParameters(Some(user_id), screen_name = None, retweets = false)
    genericNotifications(parameters)
  }

  /** Allows one to enable device notifications from the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param screen_name : The screen name of the user for whom to befriend.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def enableDeviceNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = true)
    genericNotifications(parameters)
  }

  /** Allows one to enable device notifications from the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param user_id : The ID of the user for whom to befriend.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def enableDeviceNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = true)
    genericNotifications(parameters)
  }

  /** Allows one to disable device notifications from the specified user.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param screen_name : The screen name of the user for whom to befriend.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def disableDeviceNotificationsForUser(screen_name: String): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(user_id = None, Some(screen_name), device = false)
    genericNotifications(parameters)
  }

  /** Allows one to disable device notifications from the specified user id.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/post-friendships-update</a>.
    *
    * @param user_id : The ID of the user for whom to befriend.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The user representation of the target user.
    * */
  def disableDeviceNotificationsForUserId(user_id: Long): Future[Relationship] = {
    val parameters = DeviceNotificationParameters(Some(user_id), screen_name = None, device = false)
    genericNotifications(parameters)
  }

  private def genericNotifications(parameters: NotificationParameters): Future[Relationship] = {
    import restClient._
    Post(s"$friendshipsUrl/update.json", parameters).respondAs[Relationship]
  }

  /** Returns detailed information about the relationship between two arbitrary users ids.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-show" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-show</a>.
    *
    * @param source_id : The user id of the subject user.
    * @param target_id : The user id of the target user.
    * @return :  The representation of the relationship between the two users.
    * */
  def relationshipBetweenUserIds(source_id: Long, target_id: Long): Future[RatedData[Relationship]] = {
    val parameters = RelationshipParametersByIds(source_id, target_id)
    genericGetRelationship(parameters)
  }

  /** Returns detailed information about the relationship between two arbitrary users.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-show" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-show</a>.
    *
    * @param source_screen_name : The screen name of the subject user.
    * @param target_screen_name : The screen name of the target user.
    * @return :  The representation of the relationship between the two users.
    * */
  def relationshipBetweenUsers(source_screen_name: String,
                               target_screen_name: String): Future[RatedData[Relationship]] = {
    val parameters = RelationshipParametersByNames(source_screen_name, target_screen_name)
    genericGetRelationship(parameters)
  }

  private def genericGetRelationship(parameters: RelationshipParameters): Future[RatedData[Relationship]] = {
    import restClient._
    Get(s"$friendshipsUrl/show.json", parameters).respondAsRated[Relationship]
  }

  /** Returns the relationships of the authenticating user of up to 100 user screen names.
    * Values for connections can be: `following`, `following_requested`, `followed_by`, `none`, `blocking`, `muting`.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-lookup" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-lookup</a>.
    *
    * @param screen_names :  The list of screen names.
    *                     At least 1 screen name needs to be provided. Up to 100 are allowed in a single request.
    *                     Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The sequence of the lookup relationships.
    * */
  def relationshipsWithUsers(screen_names: String*): Future[RatedData[Seq[LookupRelationship]]] = {
    require(screen_names.nonEmpty, "please, provide at least one screen name")
    val parameters = RelationshipsParameters(user_id = None, screen_name = Some(screen_names.mkString(",")))
    genericGetRelationships(parameters)
  }

  /** Returns the relationships of the authenticating user of up to 100 user ids.
    * Values for connections can be: `following`, `following_requested`, `followed_by`, `none`, `blocking`, `muting`.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-lookup" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friendships-lookup</a>.
    *
    * @param user_ids :  The list of user ids.
    *                 At least 1 user id needs to be provided. Up to 100 are allowed in a single request.
    *                 Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return :  The sequence of the lookup relationships.
    * */
  def relationshipsWithUserIds(user_ids: Long*): Future[RatedData[Seq[LookupRelationship]]] = {
    require(user_ids.nonEmpty, "please, provide at least one user id")
    val parameters = RelationshipsParameters(user_id = Some(user_ids.mkString(",")), screen_name = None)
    genericGetRelationships(parameters)
  }

  private def genericGetRelationships(
      parameters: RelationshipsParameters): Future[RatedData[Seq[LookupRelationship]]] = {
    import restClient._
    Get(s"$friendshipsUrl/lookup.json", parameters).respondAsRated[Seq[LookupRelationship]]
  }
}
