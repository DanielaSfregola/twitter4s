package com.danielasfregola.twitter4s
package http.clients.streaming.users

import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WithFilter
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.users.parameters._
import com.danielasfregola.twitter4s.http.clients.streaming.{StreamingClient, TwitterStream}
import com.danielasfregola.twitter4s.util.{ActorContextExtractor, Configurations}

import scala.concurrent.Future

trait TwitterUserClient extends StreamingClient with Configurations with ActorContextExtractor {

  private val userUrl = s"$userStreamingTwitterUrl/$twitterVersion"

  /** Starts a streaming connection from Twitter's user API. Streams messages for a single user as
    * described in <a href="https://dev.twitter.com/streaming/userstreams" target="_blank">User streams</a>.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `UserStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/get/statuses/sample" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/statuses/sample</a>.
    *
    * @param with: `Followings` by default. Specifies whether to return information for just the authenticating user,
    *              or include messages from accounts the user follows.
    *              For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters" target="_blank">
    *                https://dev.twitter.com/streaming/overview/request-parameters</a>
    * @param replies: Optional. By default @replies are only sent if the current user follows both the sender and receiver of the reply.
    *                 To receive all the replies, set the argument to `true`.
    *                 For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#replies" target="_blank">
    *                   https://dev.twitter.com/streaming/overview/request-parameters#replies</a>
    * @param track : Empty by default. Keywords to track. Phrases of keywords are specified by a comma-separated list.
    *                For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#track" target="_blank">
    *                  https://dev.twitter.com/streaming/overview/request-parameters#track</a>
    * @param locations : Empty by default. Specifies a set of bounding boxes to track.
    *                    For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#locations" target="_blank">
    *                      https://dev.twitter.com/streaming/overview/request-parameters#locations</a>
    * @param stringify_friend_ids: Optional. Specifies whether to send the Friend List preamble as an array of integers or an array of strings.
    *                              For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#stringify_friend_id" tagert="_blank">
    *                                https://dev.twitter.com/streaming/overview/request-parameters#stringify_friend_id</a>
    * @param languages : Empty by default. A comma separated list of 'BCP 47' language identifiers.
    *                    For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#language" target="_blank">
    *                      https://dev.twitter.com/streaming/overview/request-parameters#language</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages
    */
  def userEvents(`with`: WithFilter = WithFilter.Followings,
                 replies: Option[Boolean] = None,
                 track: Seq[String] = Seq.empty,
                 locations: Seq[Double] = Seq.empty,
                 stringify_friend_ids: Boolean = false,
                 languages: Seq[Language] = Seq.empty,
                 stall_warnings: Boolean = false)(f: PartialFunction[UserStreamingMessage, Unit]): Future[TwitterStream] = {
    val repliesAll = replies.flatMap(x => if (x) Some("all") else None)
    val parameters = UserParameters(`with`, repliesAll, track, locations, stringify_friend_ids, languages, stall_warnings)
    preProcessing()
    Get(s"$userUrl/user.json", parameters).processStream(f)
  }

  @deprecated("use userEvents instead", "2.2")
  def getUserEvents(`with`: WithFilter = WithFilter.Followings,
                    replies: Option[Boolean] = None,
                    track: Seq[String] = Seq.empty,
                    locations: Seq[Double] = Seq.empty,
                    stringify_friend_ids: Boolean = false,
                    languages: Seq[Language] = Seq.empty,
                    stall_warnings: Boolean = false)(f: PartialFunction[UserStreamingMessage, Unit]): Future[TwitterStream] =
    userEvents(`with`, replies, track, locations, stringify_friend_ids, languages, stall_warnings)(f)

}
