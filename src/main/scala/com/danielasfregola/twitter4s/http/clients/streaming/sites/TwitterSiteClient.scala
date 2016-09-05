package com.danielasfregola.twitter4s.http.clients.streaming.sites

import akka.actor.ActorRef
import com.danielasfregola.twitter4s.entities.enums.WithFilter
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate
import com.danielasfregola.twitter4s.http.clients.streaming.sites.parameters.SiteParameters
import com.danielasfregola.twitter4s.http.clients.StreamingOAuthClient
import com.danielasfregola.twitter4s.util.{ActorContextExtractor, Configurations}

import scala.concurrent.Future

trait TwitterSiteClient extends StreamingOAuthClient with Configurations with ActorContextExtractor {

  private[twitter4s] def createListener(f: StreamingUpdate => Unit): ActorRef

  private val siteUrl = s"$siteStreamingTwitterUrl/$twitterVersion/site.json"

  /** Starts a streaming connection from Twitter's site API. SStreams messages for a set of users,
    * as described in <a href="https://dev.twitter.com/streaming/sitestreams" target="_blank">Site streams</a>.
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
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages
    */
  def getSiteEvents(follow: Seq[Long] = Seq.empty,
                    `with`: WithFilter = WithFilter.User,
                    replies: Option[Boolean] = None,
                    stringify_friend_ids: Boolean = false,
                    stall_warnings: Boolean = false)(f: StreamingUpdate => Unit): Future[Unit] = {
    val repliesAll = replies.flatMap(x => if (x) Some("all") else None)
    val parameters = SiteParameters(follow, `with`, repliesAll, stringify_friend_ids, stall_warnings)
    val listener = createListener(f)
    streamingPipeline(listener, Get(siteUrl, parameters))
  }
}
