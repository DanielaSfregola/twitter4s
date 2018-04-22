package com.danielasfregola.twitter4s
package http.clients.streaming.sites

import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WithFilter
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.entities.streaming.SiteStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.sites.parameters.SiteParameters
import com.danielasfregola.twitter4s.http.clients.streaming.{ErrorHandler, StreamingClient, TwitterStream}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

trait TwitterSiteClient {

  protected val streamingClient: StreamingClient

  private val siteUrl = s"$siteStreamingTwitterUrl/$twitterVersion"

  /** Starts a streaming connection from Twitter's site API. Streams messages for a set of users,
    * as described in <a href="https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data" target="_blank">Site streams</a>.
    * The function returns a future of a `TwitterStream` that can be used to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `SiteStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream</a>.
    *
    * @param follow : Empty by default. List of user IDs, indicating the users whose Tweets should be delivered on the stream.
    *                 For more information <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream" target="_blank">
    *                   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream</a>
    * @param with : `User` by default. Specifies whether to return information for just the users specified in the follow parameter, or include messages from accounts they follow.
    *              For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream" target="_blank">
    *                https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream</a>
    * @param replies : Optional. By default @replies are only sent if the current user follows both the sender and receiver of the reply.
    *                 To receive all the replies, set the argument to `true`.
    *                 For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream" target="_blank">
    *                   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream</a>
    * @param stringify_friend_ids : Optional. Specifies whether to send the Friend List preamble as an array of integers or an array of strings.
    *                              For more information see <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream" tagert="_blank">
    *                                https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/site-stream</a>
    * @param languages : Empty by default. List of 'BCP 47' language identifiers.
    *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters" target="_blank">
    *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f : Function that defines how to process the received messages.
    */
  def siteEvents(
      follow: Seq[Long] = Seq.empty,
      `with`: WithFilter = WithFilter.User,
      replies: Option[Boolean] = None,
      stringify_friend_ids: Boolean = false,
      languages: Seq[Language] = Seq.empty,
      stall_warnings: Boolean = false
  )(
      f: PartialFunction[SiteStreamingMessage, Unit],
      errorHandler: PartialFunction[Throwable, Unit] = ErrorHandler.ignore
  ): Future[TwitterStream] = {
    import streamingClient._
    val repliesAll = replies.flatMap(x => if (x) Some("all") else None)
    val parameters = SiteParameters(follow, `with`, repliesAll, stringify_friend_ids, languages, stall_warnings)
    preProcessing()
    Get(s"$siteUrl/site.json", parameters).processStream(f, errorHandler)
  }
}
