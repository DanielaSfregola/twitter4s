package com.danielasfregola.twitter4s.http
package clients.rest.statuses

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.Alignment.Alignment
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WidgetType.WidgetType
import com.danielasfregola.twitter4s.entities.enums.{Alignment, Language}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters._
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `statuses` resource.
  */
trait TwitterStatusClient extends OAuthClient with Configurations {

  private val statusesUrl = s"$apiTwitterUrl/$twitterVersion/statuses"

  /** Returns the 20 most recent mentions (tweets containing a users’s @screen_name) for the authenticating user.
    * The timeline returned is the equivalent of the one seen when you view your mentions on twitter.com.
    * This method can only return up to 800 tweets.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/mentions_timeline" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/mentions_timeline</a>.
    *
    * @param count : By default it is `200`.
    *              Specifies the number of tweets to try and retrieve, up to a maximum of 200.
    *              The value of count is best thought of as a limit to the number of tweets to return because suspended or deleted content is removed after the count has been applied.
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param contributor_details : By default it is `false`.
    *                            When set to `true`, this parameter enhances the contributors element of the status response to include the screen_name of the contributor.
    *                            When set to `false`, only the user_id of the contributor is included.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will be disincluded when set to `false`.
    * @return : The sequence of tweets.
    */
  def mentionsTimeline(count: Int = 200,
                       since_id: Option[Long] = None,
                       max_id: Option[Long] = None,
                       trim_user: Boolean = false,
                       contributor_details: Boolean = false,
                       include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = MentionsParameters(count, since_id, max_id, trim_user, contributor_details, include_entities)
    Get(s"$statusesUrl/mentions_timeline.json", parameters).respondAs[Seq[Tweet]]
  }

  @deprecated("use mentionsTimeline instead", "2.2")
  def getMentionsTimeline(count: Int = 200,
                          since_id: Option[Long] = None,
                          max_id: Option[Long] = None,
                          trim_user: Boolean = false,
                          contributor_details: Boolean = false,
                          include_entities: Boolean = true): Future[Seq[Tweet]] =
    mentionsTimeline(count, since_id, max_id, trim_user, contributor_details, include_entities)

  /** Returns a collection of the most recent Tweets posted by the user indicated.
    * User timelines belonging to protected users may only be requested when the authenticated user either “owns” the timeline or is an approved follower of the owner.
    * The timeline returned is the equivalent of the one seen when you view a user’s profile on twitter.com.
    * This method can only return up to 3,200 of a user’s most recent Tweets.
    * Native retweets of other statuses by the user is included in this total, regardless of whether `include_rts` is set to false when requesting this resource.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/user_timeline" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/user_timeline</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param count : By default it is `200`.
    *              Specifies the number of tweets to try and retrieve, up to a maximum of 200.
    *              The value of count is best thought of as a limit to the number of tweets to return because suspended or deleted content is removed after the count has been applied.
    *              We include retweets in the count, even if `include_rts` is not supplied.
    *              It is recommended you always send `include_rts`=`true` when using this API method.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param exclude_replies : By default it is `false`.
    *                        This parameter will prevent replies from appearing in the returned timeline.
    *                        Using exclude_replies with the count parameter will mean you will receive up-to count tweets — this is because the count parameter retrieves that many tweets before filtering out retweets and replies.
    * @param contributor_details : By default it is `false`.
    *                            When set to `true`, this parameter enhances the contributors element of the status response to include the screen_name of the contributor.
    *                            When set to `false`, only the user_id of the contributor is included.
    * @param include_rts : By default it is `true`.
    *                    When set to `false`, the timeline will strip any native retweets (though they will still count toward both the maximal length of the timeline and the slice selected by the count parameter).
    *                    Note: If you’re using the `trim_user` parameter in conjunction with `include_rts`, the retweets will still contain a full user object.
    * @return : The sequence of tweets.
    */
  def userTimelineForUser(screen_name: String,
                          since_id: Option[Long] = None,
                          count: Int = 200,
                          max_id: Option[Long] = None,
                          trim_user: Boolean = false,
                          exclude_replies: Boolean = false,
                          contributor_details: Boolean = false,
                          include_rts: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = UserTimelineParameters(user_id = None, Some(screen_name), since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)
    genericGetUserTimeline(parameters)
  }

  @deprecated("use userTimelineForUser instead", "2.2")
  def getUserTimelineForUser(screen_name: String,
                             since_id: Option[Long] = None,
                             count: Int = 200,
                             max_id: Option[Long] = None,
                             trim_user: Boolean = false,
                             exclude_replies: Boolean = false,
                             contributor_details: Boolean = false,
                             include_rts: Boolean = true): Future[Seq[Tweet]] =
    userTimelineForUser(screen_name, since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)

  /** Returns a collection of the most recent Tweets posted by the user id indicated.
    * User timelines belonging to protected users may only be requested when the authenticated user either “owns” the timeline or is an approved follower of the owner.
    * The timeline returned is the equivalent of the one seen when you view a user’s profile on twitter.com.
    * This method can only return up to 3,200 of a user’s most recent Tweets.
    * Native retweets of other statuses by the user is included in this total, regardless of whether `include_rts` is set to false when requesting this resource.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/user_timeline" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/user_timeline</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param count : By default it is `200`.
    *              Specifies the number of tweets to try and retrieve, up to a maximum of 200.
    *              The value of count is best thought of as a limit to the number of tweets to return because suspended or deleted content is removed after the count has been applied.
    *              We include retweets in the count, even if `include_rts` is not supplied.
    *              It is recommended you always send `include_rts`=`true` when using this API method.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param exclude_replies : By default it is `false`.
    *                        This parameter will prevent replies from appearing in the returned timeline.
    *                        Using exclude_replies with the count parameter will mean you will receive up-to count tweets — this is because the count parameter retrieves that many tweets before filtering out retweets and replies.
    * @param contributor_details : By default it is `false`.
    *                            When set to `true`, this parameter enhances the contributors element of the status response to include the screen_name of the contributor.
    *                            When set to `false`, only the user_id of the contributor is included.
    * @param include_rts : By default it is `true`.
    *                    When set to `false`, the timeline will strip any native retweets (though they will still count toward both the maximal length of the timeline and the slice selected by the count parameter).
    *                    Note: If you’re using the `trim_user` parameter in conjunction with `include_rts`, the retweets will still contain a full user object.
    * @return : The sequence of tweets.
    */
  def userTimelineForUserId(user_id: Long,
                            since_id: Option[Long] = None,
                            count: Int = 200,
                            max_id: Option[Long] = None,
                            trim_user: Boolean = false,
                            exclude_replies: Boolean = false,
                            contributor_details: Boolean = false,
                            include_rts: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = UserTimelineParameters(Some(user_id), None, since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)
    genericGetUserTimeline(parameters)
  }

  @deprecated("use userTimelineForUserId instead", "2.2")
  def getUserTimelineForUserId(user_id: Long,
                               since_id: Option[Long] = None,
                               count: Int = 200,
                               max_id: Option[Long] = None,
                               trim_user: Boolean = false,
                               exclude_replies: Boolean = false,
                               contributor_details: Boolean = false,
                               include_rts: Boolean = true): Future[Seq[Tweet]] =
    userTimelineForUserId(user_id, since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)

  private def genericGetUserTimeline(parameters: UserTimelineParameters): Future[Seq[Tweet]] =
    Get(s"$statusesUrl/user_timeline.json", parameters).respondAs[Seq[Tweet]]

  /** Returns a collection of the most recent Tweets and retweets posted by the authenticating user and the users they follow.
    * The home timeline is central to how most users interact with the Twitter service.
    * Up to 800 Tweets are obtainable on the home timeline.
    * It is more volatile for users that follow many users or follow users who tweet frequently.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/home_timeline" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/home_timeline</a>.
    *
    * @param count : By default it is `20`.
    *              Specifies the number of records to retrieve. Must be less than or equal to 200.
    *              The value of count is best thought of as a limit to the number of tweets to return because suspended or deleted content is removed after the count has been applied.
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param exclude_replies : By default it is `false`.
    *                        This parameter will prevent replies from appearing in the returned timeline.
    *                        Using exclude_replies with the count parameter will mean you will receive up-to count tweets — this is because the count parameter retrieves that many tweets before filtering out retweets and replies.
    * @param contributor_details : By default it is `false`.
    *                            When set to `true`, this parameter enhances the contributors element of the status response to include the screen_name of the contributor.
    *                            When set to `false`, only the user_id of the contributor is included.
    * @param include_entities : By default it is `true`.
    *                    When set to `false`, The parameters node will be disincluded when set to false.
    * @return : The sequence of tweets.
    */
  def homeTimeline(count: Int = 20,
                   since_id: Option[Long] = None,
                   max_id: Option[Long] = None,
                   trim_user: Boolean = false,
                   exclude_replies: Boolean = false,
                   contributor_details: Boolean = false,
                   include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = HomeTimelineParameters(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)
    Get(s"$statusesUrl/home_timeline.json", parameters).respondAs[Seq[Tweet]]
  }

  @deprecated("use homeTimeline instead", "2.2")
  def getHomeTimeline(count: Int = 20,
                      since_id: Option[Long] = None,
                      max_id: Option[Long] = None,
                      trim_user: Boolean = false,
                      exclude_replies: Boolean = false,
                      contributor_details: Boolean = false,
                      include_entities: Boolean = true): Future[Seq[Tweet]] =
    homeTimeline(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)

  /** Returns the most recent tweets authored by the authenticating user that have been retweeted by others.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/retweets_of_me" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/retweets_of_me</a>.
    *
    * @param count : By default it is `20`.
    *              Specifies the number of records to retrieve. Must be less than or equal to 100.
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param exclude_replies : By default it is `false`.
    *                        This parameter will prevent replies from appearing in the returned timeline.
    *                        Using exclude_replies with the count parameter will mean you will receive up-to count tweets — this is because the count parameter retrieves that many tweets before filtering out retweets and replies.
    * @param contributor_details : By default it is `false`.
    *                            When set to `true`, this parameter enhances the contributors element of the status response to include the screen_name of the contributor.
    *                            When set to `false`, only the user_id of the contributor is included.
    * @param include_entities : By default it is `true`.
    *                    When set to `false`, The parameters node will be disincluded when set to false.
    * @return : The sequence of tweets.
    */
  def retweetsOfMe(count: Int = 20,
                   since_id: Option[Long] = None,
                   max_id: Option[Long] = None,
                   trim_user: Boolean = false,
                   exclude_replies: Boolean = false,
                   contributor_details: Boolean = false,
                   include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = RetweetsOfMeParameters(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)
    Get(s"$statusesUrl/retweets_of_me.json", parameters).respondAs[Seq[Tweet]]
  }

  @deprecated("use retweetsOfMe instead", "2.2")
  def getRetweetsOfMe(count: Int = 20,
                      since_id: Option[Long] = None,
                      max_id: Option[Long] = None,
                      trim_user: Boolean = false,
                      exclude_replies: Boolean = false,
                      contributor_details: Boolean = false,
                      include_entities: Boolean = true): Future[Seq[Tweet]] =
    retweetsOfMe(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)

  /** Returns a collection of the 100 most recent retweets of the tweet specified by the id parameter.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/retweets/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/retweets/%3Aid</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param count : By default it is `100`.
    *              Specifies the number of records to retrieve. Must be less than or equal to 100.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @return : The sequence of tweets.
    */
  def retweets(id: Long,
               count: Int = 100,
               trim_user: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = RetweetsParameters(count, trim_user)
    Get(s"$statusesUrl/retweets/$id.json", parameters).respondAs[Seq[Tweet]]
  }

  @deprecated("use retweets instead", "2.2")
  def getRetweets(id: Long,
                  count: Int = 100,
                  trim_user: Boolean = false): Future[Seq[Tweet]] =
    retweets(id, count, trim_user)


  /** Returns a single Tweet, specified by the id parameter. The Tweet’s author will also be embedded within the tweet.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/show/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/show/%3Aid</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param include_my_retweet : By default it is `false`.
    *                           When set to `true`, any Tweets returned that have been retweeted by the authenticating user will include an additional `current_user_retweet` node, containing the ID of the source status for the retweet.
    * @param include_entities : By default it is `true`.
    *                         When set to `false`, The parameters node will be disincluded when set to false.
    * @return : The representation of the tweet.
    */
  def getTweet(id: Long,
               trim_user: Boolean = false,
               include_my_retweet: Boolean = false,
               include_entities: Boolean = true): Future[Tweet] = {
    val parameters = ShowParameters(id, trim_user, include_my_retweet, include_entities)
    Get(s"$statusesUrl/show.json", parameters).respondAs[Tweet]
  }


  /** Destroys the status specified by the required ID parameter.
    * The authenticating user must be the author of the specified status.
    * Returns the destroyed status if successful.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/statuses/destroy/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/statuses/destroy/%3Aid</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @return : The representation of the deleted tweet.
    */
  def deleteTweet(id: Long,
                  trim_user: Boolean = false): Future[Tweet] = {
    val parameters = PostParameters(trim_user)
    Post(s"$statusesUrl/destroy/$id.json", parameters).respondAs[Tweet]
  }

  /** Updates the authenticating user’s current status, also known as Tweeting.
    * For each update attempt, the update text is compared with the authenticating user’s recent Tweets.
    * Any attempt that would result in duplication will be blocked, resulting in a `TwitterException` error.
    * Therefore, a user cannot submit the same status twice in a row.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/statuses/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/statuses/update</a>.
    *
    * @param status : The text of your status update, typically up to 140 characters.
    * @param in_reply_to_status_id : Optional, by default it is `None`.
    *                              The ID of an existing status that the update is in reply to.
    *                              Note that this parameter will be ignored unless the author of the tweet this parameter references is mentioned within the status text.
    *                              Therefore, you must include @username, where username is the author of the referenced tweet, within the update.
    * @param possibly_sensitive : By default it is `false`.
    *                           If you upload Tweet media that might be considered sensitive content such as nudity, violence, or medical procedures, you should set this value to `true`.
    * @param latitude : Optional, by default it is `None`.
    *                 The latitude of the location this tweet refers to.
    *                 This parameter will be ignored unless it is inside the range -90.0 to +90.0 (North is positive) inclusive.
    *                 It will also be ignored if there isn’t a corresponding `longitude` parameter.
    * @param longitude : Optional, by default is `None`.
    *                  The longitude of the location this tweet refers to.
    *                  The valid ranges for longitude is -180.0 to +180.0 (East is positive) inclusive.
    *                  This parameter will be ignored if outside that range, if it is not a number, if geo_enabled is disabled, or if there not a corresponding `latitude` parameter.
    * @param place_id : Optional, by default it is `None`.
    *                 A place in the world identified by an id.
    * @param display_coordinates : By default it is `false`.
    *                            Whether or not to put a pin on the exact coordinates a tweet has been sent from.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param media_ids : By default it is an empty sequence.
    *                  A list of media_ids to associate with the Tweet. You may include up to 4 photos or 1 animated GIF or 1 video in a Tweet.
    * @return : The representation of the created tweet.
    */
  def createTweet(status: String,
                  in_reply_to_status_id: Option[Long] = None,
                  possibly_sensitive: Boolean = false,
                  latitude: Option[Long] = None,
                  longitude: Option[Long] = None,
                  place_id: Option[String] = None,
                  display_coordinates: Boolean = false,
                  trim_user: Boolean = false,
                  media_ids: Seq[Long] = Seq.empty): Future[Tweet] = {
    val entity = TweetUpdate(status, in_reply_to_status_id, possibly_sensitive, latitude, longitude, place_id, display_coordinates, trim_user, media_ids)
    Post(s"$statusesUrl/update.json", entity).respondAs[Tweet]
  }

  @deprecated("use createTweet instead", "2.2")
  def tweet(status: String,
            in_reply_to_status_id: Option[Long] = None,
            possibly_sensitive: Boolean = false,
            latitude: Option[Long] = None,
            longitude: Option[Long] = None,
            place_id: Option[String] = None,
            display_coordinates: Boolean = false,
            trim_user: Boolean = false,
            media_ids: Seq[Long] = Seq.empty): Future[Tweet] =
    createTweet(status, in_reply_to_status_id, possibly_sensitive, latitude, longitude, place_id, display_coordinates, trim_user, media_ids)

  /** Sends a direct message to a specified user.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/statuses/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/statuses/update</a>.
    *
    * @param message : The text of your direct message.
    * @param screen_name : : The screen name of the user that should receive the message.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param in_reply_to_status_id : Optional, by default it is `None`.
    *                              The ID of an existing status that the update is in reply to.
    *                              Note that this parameter will be ignored unless the author of the tweet this parameter references is mentioned within the status text.
    *                              Therefore, you must include @username, where username is the author of the referenced tweet, within the update.
    * @param possibly_sensitive : By default it is `false`.
    *                           If you upload Tweet media that might be considered sensitive content such as nudity, violence, or medical procedures, you should set this value to `true`.
    * @param latitude : Optional, by default it is `None`.
    *                 The latitude of the location this tweet refers to.
    *                 This parameter will be ignored unless it is inside the range -90.0 to +90.0 (North is positive) inclusive.
    *                 It will also be ignored if there isn’t a corresponding `longitude` parameter.
    * @param longitude : Optional, by default is `None`.
    *                  The longitude of the location this tweet refers to.
    *                  The valid ranges for longitude is -180.0 to +180.0 (East is positive) inclusive.
    *                  This parameter will be ignored if outside that range, if it is not a number, if geo_enabled is disabled, or if there not a corresponding `latitude` parameter.
    * @param place_id : Optional, by default it is `None`.
    *                 A place in the world identified by an id.
    * @param display_coordinates : By default it is `false`.
    *                            Whether or not to put a pin on the exact coordinates a tweet has been sent from.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @param media_ids : By default it is an empty sequence.
    *                  A list of media_ids to associate with the Tweet. You may include up to 4 photos or 1 animated GIF or 1 video in a Tweet.
    * @return : The representation of the created direct message.
    */
  def createDirectMessageAsTweet(message: String,
                                 screen_name: String,
                                 in_reply_to_status_id: Option[Long] = None,
                                 possibly_sensitive: Boolean = false,
                                 latitude: Option[Long] = None,
                                 longitude: Option[Long] = None,
                                 place_id: Option[String] = None,
                                 display_coordinates: Boolean = false,
                                 trim_user: Boolean = false,
                                 media_ids: Seq[Long] = Seq.empty): Future[Tweet] = {
    val directMessage = s"D $screen_name $message"
    createTweet(directMessage, in_reply_to_status_id, possibly_sensitive, latitude, longitude, place_id, display_coordinates, trim_user, media_ids)
  }

  /** Retweets a tweet. Returns the original tweet with retweet details embedded.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/statuses/retweet/%3Aid" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/statuses/retweet/%3Aid</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @return : The representation of the original tweet with retweet details embedded.
    */
  def retweet(id: Long, trim_user: Boolean = false): Future[Tweet] = {
    val parameters = PostParameters(trim_user)
    Post(s"$statusesUrl/retweet/$id.json", parameters).respondAs[Tweet]
  }

  /** Returns a single Tweet, specified by the Tweet ID, in an oEmbed-compatible format.
    * The returned HTML snippet will be automatically recognized as an Embedded Tweet when Twitter’s widget JavaScript is included on the page.
    * The oEmbed endpoint allows customization of the final appearance of an Embedded Tweet by setting the corresponding properties in HTML markup to be interpreted by Twitter’s JavaScript bundled with the HTML response by default.
    * The format of the returned markup may change over time as Twitter adds new features or adjusts its Tweet representation.
    * The Tweet fallback markup is meant to be cached on your servers for up to the suggested cache lifetime specified in the cache_age.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/oembed" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/oembed</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param max_width : Optional, by default it is `None`.
    *                  The maximum width of a rendered Tweet in whole pixels. This value must be between 220 and 550 inclusive.
    *                  A supplied value under or over the allowed range will be returned as the minimum or maximum supported width respectively; the reset width value will be reflected in the returned width property.
    *                  Note that Twitter does not support the oEmbed max_height parameter.
    *                  Tweets are fundamentally text, and are therefore of unpredictable height that cannot be scaled like an image or video.
    *                  Relatedly, the oEmbed response will not provide a value for height.
    *                  Implementations that need consistent heights for Tweets should refer to the hide_thread and hide_media parameters below.
    * @param hide_media : By default, it is `false`.
    *                   When set to `true`, links in a Tweet are not expanded to photo, video, or link previews.
    * @param hide_thread : By default, it is `false`.
    *                    When set to `true`, a collapsed version of the previous Tweet in a conversation thread will not be displayed when the requested Tweet is in reply to another Tweet.
    * @param omit_script : By default, it is `false`.
    *                    When set to `true`, the `<script>` responsible for loading widgets.js will not be returned. Your webpages should include their own reference to widgets.js for use across all Twitter widgets including Embedded Tweets.
    * @param alignment : By default it is `None`, meaning no alignment styles are specified for the Tweet.
    *                  Specifies whether the embedded Tweet should be floated left, right, or center in the page relative to the parent element.
    * @param related : By default it is an empty sequence.
    *                A sequence of Twitter usernames related to your content.
    *                This value will be forwarded to Tweet action intents if a viewer chooses to reply, favorite, or retweet the embedded Tweet.
    * @param language : By default it is `English`.
    *                 Request returned HTML and a rendered Tweet in the specified Twitter language supported by embedded Tweets.
    * @param widget_type : Optional, by default it is `None`.
    *                    Set to `video` to return a Twitter Video embed for the given Tweet.
    * @param hide_tweet : By default, it is `false`.
    *                   Applies to video type only. Set to `true` to link directly to the Tweet URL instead of displaying a Tweet overlay when a viewer clicks on the Twitter bird logo.
    * @return : The representation of embedded tweet.
    */
  def oembedTweetById(id: Long,
                      max_width: Option[Int] = None,
                      hide_media: Boolean = false,
                      hide_thread: Boolean = false,
                      hide_tweet: Boolean = false,
                      omit_script: Boolean = false,
                      alignment: Alignment = Alignment.None,
                      related: Seq[String] = Seq.empty,
                      language: Language = Language.English,
                      widget_type: Option[WidgetType] = None): Future[OEmbedTweet] = {
    val parameters = OEmbedParameters(Some(id), url = None, max_width, hide_media, hide_thread, omit_script, alignment, related, language, widget_type, hide_tweet)
    genericGetOembeddedTweet(parameters)
  }

  @deprecated("use oembedTweetById instead", "2.2")
  def getOembedTweetById(id: Long,
                         max_width: Option[Int] = None,
                         hide_media: Boolean = false,
                         hide_thread: Boolean = false,
                         hide_tweet: Boolean = false,
                         omit_script: Boolean = false,
                         alignment: Alignment = Alignment.None,
                         related: Seq[String] = Seq.empty,
                         language: Language = Language.English,
                         widget_type: Option[WidgetType] = None): Future[OEmbedTweet] =
    oembedTweetById(id, max_width, hide_media, hide_thread, hide_tweet, omit_script, alignment, related, language, widget_type)

  /** Returns a single Tweet, specified by a Tweet web URL, in an oEmbed-compatible format.
    * The returned HTML snippet will be automatically recognized as an Embedded Tweet when Twitter’s widget JavaScript is included on the page.
    * The oEmbed endpoint allows customization of the final appearance of an Embedded Tweet by setting the corresponding properties in HTML markup to be interpreted by Twitter’s JavaScript bundled with the HTML response by default.
    * The format of the returned markup may change over time as Twitter adds new features or adjusts its Tweet representation.
    * The Tweet fallback markup is meant to be cached on your servers for up to the suggested cache lifetime specified in the cache_age.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/oembed" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/oembed</a>.
    *
    * @param url : The URL of the Tweet to be embedded.
    * @param max_width : Optional, by default it is `None`.
    *                  The maximum width of a rendered Tweet in whole pixels. This value must be between 220 and 550 inclusive.
    *                  A supplied value under or over the allowed range will be returned as the minimum or maximum supported width respectively; the reset width value will be reflected in the returned width property.
    *                  Note that Twitter does not support the oEmbed max_height parameter.
    *                  Tweets are fundamentally text, and are therefore of unpredictable height that cannot be scaled like an image or video.
    *                  Relatedly, the oEmbed response will not provide a value for height.
    *                  Implementations that need consistent heights for Tweets should refer to the hide_thread and hide_media parameters below.
    * @param hide_media : By default, it is `false`.
    *                   When set to `true`, links in a Tweet are not expanded to photo, video, or link previews.
    * @param hide_thread : By default, it is `false`.
    *                    When set to `true`, a collapsed version of the previous Tweet in a conversation thread will not be displayed when the requested Tweet is in reply to another Tweet.
    * @param omit_script : By default, it is `false`.
    *                    When set to `true`, the `<script>` responsible for loading widgets.js will not be returned. Your webpages should include their own reference to widgets.js for use across all Twitter widgets including Embedded Tweets.
    * @param alignment : By default it is `None`, meaning no alignment styles are specified for the Tweet.
    *                  Specifies whether the embedded Tweet should be floated left, right, or center in the page relative to the parent element.
    * @param related : By default it is an empty sequence.
    *                A sequence of Twitter usernames related to your content.
    *                This value will be forwarded to Tweet action intents if a viewer chooses to reply, favorite, or retweet the embedded Tweet.
    * @param language : By default it is `English`.
    *                 Request returned HTML and a rendered Tweet in the specified Twitter language supported by embedded Tweets.
    * @param widget_type : Optional, by default it is `None`.
    *                    Set to `video` to return a Twitter Video embed for the given Tweet.
    * @param hide_tweet : By default, it is `false`.
    *                   Applies to video type only. Set to `true` to link directly to the Tweet URL instead of displaying a Tweet overlay when a viewer clicks on the Twitter bird logo.
    * @return : The representation of embedded tweet.
    */
  def oembedTweetByUrl(url: String,
                       max_width: Option[Int] = None,
                       hide_media: Boolean = false,
                       hide_thread: Boolean = false,
                       hide_tweet: Boolean = false,
                       omit_script: Boolean = false,
                       alignment: Alignment = Alignment.None,
                       related: Seq[String] = Seq.empty,
                       language: Language = Language.English,
                       widget_type: Option[WidgetType] = None): Future[OEmbedTweet] = {
    val parameters = OEmbedParameters(id = None, Some(url), max_width, hide_media, hide_thread, omit_script, alignment, related, language, widget_type, hide_tweet)
    genericGetOembeddedTweet(parameters)
  }

  @deprecated("use oembedTweetByUrl instead", "2.2")
  def getOembedTweetByUrl(url: String,
                          max_width: Option[Int] = None,
                          hide_media: Boolean = false,
                          hide_thread: Boolean = false,
                          hide_tweet: Boolean = false,
                          omit_script: Boolean = false,
                          alignment: Alignment = Alignment.None,
                          related: Seq[String] = Seq.empty,
                          language: Language = Language.English,
                          widget_type: Option[WidgetType] = None): Future[OEmbedTweet] =
    oembedTweetByUrl(url, max_width, hide_media, hide_thread, hide_tweet, omit_script, alignment, related, language, widget_type)

  private def genericGetOembeddedTweet(parameters: OEmbedParameters): Future[OEmbedTweet] =
    Get(s"$statusesUrl/oembed.json", parameters).respondAs[OEmbedTweet]

  /** Returns a collection of up to 100 user IDs belonging to users who have retweeted the tweet specified by the id parameter.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/retweeters/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/retweeters/ids</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param count : By default, it is `100`.
    *              Specifies the number of ids to retrieve. Must be less than or equal to 100.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The representation of the retweeter ids.
    */
  def retweeterIds(id: Long, count: Int = 100, cursor: Long = -1): Future[UserIds] = {
    val parameters = RetweetersIdsParameters(id, count, cursor, stringify_ids = false)
    genericGetRetweeterIds[UserIds](parameters)
  }

  @deprecated("use retweeterIds instead", "2.2")
  def getRetweeterIds(id: Long, count: Int = 100, cursor: Long = -1): Future[UserIds] =
    retweeterIds(id, count, cursor)

  /** Returns a collection of up to 100 user stringified IDs belonging to users who have retweeted the tweet specified by the id parameter.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/retweeters/ids" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/retweeters/ids</a>.
    *
    * @param id : The numerical ID of the desired status.
    * @param count : By default, it is `100`.
    *              Specifies the number of ids to retrieve. Must be less than or equal to 100.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The representation of the retweeter stringified ids.
    */
  def retweeterStringifiedIds(id: Long, count: Int = 100, cursor: Long = -1): Future[UserStringifiedIds] = {
    val parameters = RetweetersIdsParameters(id, count, cursor, stringify_ids = true)
    genericGetRetweeterIds[UserStringifiedIds](parameters)
  }

  @deprecated("use retweeterStringifiedIds instead", "2.2")
  def getRetweeterStringifiedIds(id: Long, count: Int = 100, cursor: Long = -1): Future[UserStringifiedIds] =
    retweeterStringifiedIds(id, count, cursor)

  private def genericGetRetweeterIds[T: Manifest](parameters: RetweetersIdsParameters): Future[T] =
    Get(s"$statusesUrl/retweeters/ids.json", parameters).respondAs[T]

  /** Returns fully-hydrated tweet objects for up to 100 tweets per request, as specified by sequence of values passed to the id parameter.
    * This method is especially useful to get the details (hydrate) a collection of Tweet IDs.
    * Tweets that do not exist or cannot be viewed by the current user will not be returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/lookup</a>.
    *
    * @param ids : A sequence of tweet IDs, up to 100 are allowed in a single request.
    * @return : The representation of the lookup tweets.
    */
  def tweetLookup(ids: Long*): Future[Seq[LookupTweet]] = tweetLookup(ids)

  @deprecated("use tweetLookup instead", "2.2")
  def getTweetLookup(ids: Long*): Future[Seq[LookupTweet]] = tweetLookup(ids:_*)

  /** Returns fully-hydrated tweet objects for up to 100 tweets per request, as specified by sequence of values passed to the id parameter.
    * This method is especially useful to get the details (hydrate) a collection of Tweet IDs.
    * Tweets that do not exist or cannot be viewed by the current user will not be returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/lookup</a>.
    *
    * @param ids : A sequence of tweet IDs, up to 100 are allowed in a single request.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will be disincluded when set to `false`.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @return : The representation of the lookup tweets.
    */
  def tweetLookup(ids: Seq[Long],
                  include_entities: Boolean = true,
                  trim_user: Boolean = false): Future[Seq[LookupTweet]] = {
    require(!ids.isEmpty, "please, provide at least one status id to lookup")
    val parameters = LookupParameters(ids.mkString(","), include_entities, trim_user, map = false)
    genericGetTweetLookup[Seq[LookupTweet]](parameters)
  }

  @deprecated("use tweetLookup instead", "2.2")
  def getTweetLookup(ids: Seq[Long],
                     include_entities: Boolean = true,
                     trim_user: Boolean = false): Future[Seq[LookupTweet]] =
    tweetLookup(ids, include_entities, trim_user)

  /** Returns fully-hydrated tweet objects for up to 100 tweets per request, as specified by sequence of values passed to the id parameter.
    * This method is especially useful to get the details (hydrate) a collection of Tweet IDs.
    * Tweets ids will be used to create a map using the tweet id as key and tweet as corresponding body.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/lookup</a>.
    *
    * @param ids : A sequence of tweet IDs, up to 100 are allowed in a single request.
    * @return : The representation of the lookup tweets mapped by id.
    */
  def tweetLookupMapped(ids: Long*): Future[LookupMapped] = tweetLookupMapped(ids)

  @deprecated("use tweetLookupMapped instead", "2.2")
  def getTweetLookupMapped(ids: Long*): Future[LookupMapped] = tweetLookupMapped(ids:_*)

  /** Returns fully-hydrated tweet objects for up to 100 tweets per request, as specified by sequence of values passed to the id parameter.
    * This method is especially useful to get the details (hydrate) a collection of Tweet IDs.
    * Tweets ids will be used to create a map using the tweet id as key and tweet as corresponding body.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/statuses/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/statuses/lookup</a>.
    *
    * @param ids : A sequence of tweet IDs, up to 100 are allowed in a single request.
    * @param include_entities : By default it is `true`.
    *                         The parameters node will be disincluded when set to `false`.
    * @param trim_user : By default it is `false`.
    *                  When set to `true`, each tweet returned in a timeline will include a user object including only the status authors numerical ID.
    *                  Set this parameter to `false` to receive the complete user object.
    * @return : The representation of the lookup tweets mapped by id.
    */
  def tweetLookupMapped(ids: Seq[Long],
                        include_entities: Boolean = true,
                        trim_user: Boolean = false): Future[LookupMapped] = {
    val parameters = LookupParameters(ids.mkString(","), include_entities, trim_user, map = true)
    genericGetTweetLookup[LookupMapped](parameters)
  }

  @deprecated("use tweetLookupMapped instead", "2.2")
  def getTweetLookupMapped(ids: Seq[Long],
                           include_entities: Boolean = true,
                           trim_user: Boolean = false): Future[LookupMapped] =
    tweetLookupMapped(ids, include_entities, trim_user)

  private def genericGetTweetLookup[T: Manifest](parameters: LookupParameters): Future[T] =
    Get(s"$statusesUrl/lookup.json", parameters).respondAs[T]
}






