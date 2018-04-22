package com.danielasfregola.twitter4s
package http.clients.streaming.users

import com.danielasfregola.twitter4s.entities.GeoBoundingBox
import com.danielasfregola.twitter4s.entities.GeoBoundingBox.toLngLatPairs
import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.entities.enums.{FilterLevel, WithFilter}
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.users.parameters._
import com.danielasfregola.twitter4s.http.clients.streaming.{ErrorHandler, StreamingClient, TwitterStream}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

trait TwitterUserClient {

  protected val streamingClient: StreamingClient

  private val userUrl = s"$userStreamingTwitterUrl/$twitterVersion"

  /** Starts a streaming connection from Twitter's user API. Streams messages for a single user as
    * described in <a href="https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data" target="_blank">User streams</a>.
    * The function returns a future of a `TwitterStream` that can be used to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `UserStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>.
    *
    * @param with : `Followings` by default. Specifies whether to return information for just the authenticating user,
    *              or include messages from accounts the user follows.
    *              For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *                https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param replies: Optional. By default @replies are only sent if the current user follows both the sender and receiver of the reply.
    *                 To receive all the replies, set the argument to `true`.
    *                 For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *                   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param tracks : Empty by default. List of phrases which will be used to determine what Tweets will be delivered on the stream.
    *                 Each phrase must be between 1 and 60 bytes, inclusive.
    *                 For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *                  https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param locations : Empty by default. Specifies a set of bounding boxes to track.
    *                    For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *                      https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param stringify_friend_ids: Optional. Specifies whether to send the Friend List preamble as an array of integers or an array of strings.
    *                              For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" tagert="_blank">
    *                                https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param languages : Empty by default. List of 'BCP 47' language identifiers.
    *                    For more information <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream" target="_blank">
    *                      https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/user-stream</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f : Function that defines how to process the received messages.
    */
  def userEvents(
      `with`: WithFilter = WithFilter.Followings,
      replies: Option[Boolean] = None,
      tracks: Seq[String] = Seq.empty,
      locations: Seq[GeoBoundingBox] = Seq.empty,
      stringify_friend_ids: Boolean = false,
      languages: Seq[Language] = Seq.empty,
      stall_warnings: Boolean = false,
      filter_level: FilterLevel = FilterLevel.None
  )(
      f: PartialFunction[UserStreamingMessage, Unit],
      errorHandler: PartialFunction[Throwable, Unit] = ErrorHandler.ignore
  ): Future[TwitterStream] = {
    import streamingClient._
    val repliesAll = replies.flatMap(x => if (x) Some("all") else None)
    val parameters = UserParameters(`with`,
                                    repliesAll,
                                    tracks,
                                    toLngLatPairs(locations),
                                    stringify_friend_ids,
                                    languages,
                                    stall_warnings,
                                    filter_level)
    preProcessing()
    Get(s"$userUrl/user.json", parameters).processStream(f, errorHandler)
  }
}
