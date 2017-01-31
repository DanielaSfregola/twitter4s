package com.danielasfregola.twitter4s
package http.clients.streaming.sites

import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WithFilter
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.entities.streaming.SiteStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.sites.parameters.SiteParameters
import com.danielasfregola.twitter4s.http.clients.streaming.{StreamingClient, TwitterStream}

import scala.concurrent.Future

private[twitter4s] trait TwitterSiteClient {

  protected val streamingClient: StreamingClient

  private val siteUrl = s"$siteStreamingTwitterUrl/$twitterVersion"

  /** Starts a streaming connection from Twitter's site API. SStreams messages for a set of users,
    * as described in <a href="https://dev.twitter.com/streaming/sitestreams" target="_blank">Site streams</a>.
    * The function returns a future of a `TwitterStream` that can be use to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `SiteStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/get/site" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/site</a>.
    *
    * @param follow : Empty by default. A comma separated list of user IDs, indicating the users to return statuses for in the stream.
    *                 For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#follow" target="_blank">
    *                   https://dev.twitter.com/streaming/overview/request-parameters#follow</a>
    * @param with: `User` by default. Specifies whether to return information for just the users specified in the follow parameter, or include messages from accounts they follow.
    *              For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters" target="_blank">
    *                https://dev.twitter.com/streaming/overview/request-parameters</a>
    * @param replies: Optional. By default @replies are only sent if the current user follows both the sender and receiver of the reply.
    *                 To receive all the replies, set the argument to `true`.
    *                 For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#replies" target="_blank">
    *                   https://dev.twitter.com/streaming/overview/request-parameters#replies</a>
    * @param stringify_friend_ids: Optional. Specifies whether to send the Friend List preamble as an array of integers or an array of strings.
    *                              For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#stringify_friend_id" tagert="_blank">
    *                                https://dev.twitter.com/streaming/overview/request-parameters#stringify_friend_id</a>
    * @param languages : Empty by default. A comma separated list of 'BCP 47' language identifiers.
    *                    For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#language" target="_blank">
    *                      https://dev.twitter.com/streaming/overview/request-parameters#language</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages
    */
  def siteEvents(follow: Seq[Long] = Seq.empty,
                 `with`: WithFilter = WithFilter.User,
                 replies: Option[Boolean] = None,
                 stringify_friend_ids: Boolean = false,
                 languages: Seq[Language] = Seq.empty,
                 stall_warnings: Boolean = false)(f: PartialFunction[SiteStreamingMessage, Unit]): Future[TwitterStream] = {
    import streamingClient._
    val repliesAll = replies.flatMap(x => if (x) Some("all") else None)
    val parameters = SiteParameters(follow, `with`, repliesAll, stringify_friend_ids, languages, stall_warnings)
    preProcessing()
    Get(s"$siteUrl/site.json", parameters).processStream(f)
  }

  @deprecated("use siteEvents instead", "2.2")
  def getSiteEvents(follow: Seq[Long] = Seq.empty,
                    `with`: WithFilter = WithFilter.User,
                    replies: Option[Boolean] = None,
                    stringify_friend_ids: Boolean = false,
                    languages: Seq[Language] = Seq.empty,
                    stall_warnings: Boolean = false)(f: PartialFunction[SiteStreamingMessage, Unit]): Future[TwitterStream] =
    siteEvents(follow, `with`, replies, stringify_friend_ids, languages, stall_warnings)(f)
}
