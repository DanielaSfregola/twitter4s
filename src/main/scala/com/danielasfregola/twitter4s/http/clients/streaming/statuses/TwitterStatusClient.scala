package com.danielasfregola.twitter4s.http.clients.streaming.statuses

import akka.actor.ActorRef
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate
import com.danielasfregola.twitter4s.http.clients.StreamingOAuthClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters._
import com.danielasfregola.twitter4s.util.{ActorContextExtractor, Configurations}

import scala.concurrent.Future

trait TwitterStatusClient extends StreamingOAuthClient with Configurations with ActorContextExtractor {

  private[twitter4s] def createListener(f: StreamingUpdate => Unit): ActorRef

  private val filterUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/filter.json"
  private val sampleUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/sample.json"
  private val firehoseUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/firehose.json"

  /** Starts a streaming connection from Twitter's public API, filtered with the 'follow', 'track' and 'location' parameters.
    * Although all of those three params are optional, at least one must be specified.
    * The track, follow, and locations fields should be considered to be combined with an OR operator.
    * The function only returns an empty future, that can be used to track failures in establishing the initial connection.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `StreamingUpdate[StreamingEvent]`
    * and processed accordingly to the function `f`.
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/post/statuses/filter" target="_blank">
    *   https://dev.twitter.com/streaming/reference/post/statuses/filter</a>.
    *
    * @param follow : Empty by default. A comma separated list of user IDs, indicating the users to return statuses for in the stream.
    *                 For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#follow" target="_blank">
    *                   https://dev.twitter.com/streaming/overview/request-parameters#follow</a>
    * @param track : Empty by default. Keywords to track. Phrases of keywords are specified by a comma-separated list.
    *                For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#track" target="_blank">
    *                  https://dev.twitter.com/streaming/overview/request-parameters#track</a>
    * @param locations : Empty by default. Specifies a set of bounding boxes to track.
    *                    For more information <a href="https://dev.twitter.com/streaming/overview/request-parameters#locations" target="_blank">
    *                      https://dev.twitter.com/streaming/overview/request-parameters#locations</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages
    */
  def getStatusesFilter(follow: Seq[Long] = Seq.empty,
                        track: Seq[String] = Seq.empty,
                        locations: Seq[Double] = Seq.empty,
                        stall_warnings: Boolean = false)(f: StreamingUpdate => Unit): Future[Unit] = {
    require(follow.nonEmpty || track.nonEmpty || locations.nonEmpty, "At least one of 'follow', 'track' or 'locations' needs to be non empty")
    val parameters = StatusFilterParameters(follow, track, locations, stall_warnings)
    val listener = createListener(f)
    streamingPipeline(listener, Post(filterUrl, parameters.asInstanceOf[Product]))
  }

  /** Starts a streaming connection from Twitter's public API, which is a a small random sample of all public statuses.
    * The Tweets returned by the default access level are the same, so if two different clients connect to this endpoint, they will see the same Tweets.
    * The function only returns an empty future, that can be used to track failures in establishing the initial connection.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `StreamingUpdate[StreamingEvent]`
    * and processed accordingly to the function `f`.
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/get/statuses/sample" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/statuses/sample</a>.
    *
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages
    */
  def getStatusesSample(stall_warnings: Boolean = false)(f: StreamingUpdate => Unit): Future[Unit] = {
    val parameters = StatusSampleParameters(stall_warnings)
    val listener = createListener(f)
    streamingPipeline(listener, Get(sampleUrl, parameters))
  }

  /** Starts a streaming connection from Twitter's firehouse API of all public statuses.
    * Few applications require this level of access.
    * Creative use of a combination of other resources and various access levels can satisfy nearly every application use case.
    * For more information see <a href="https://dev.twitter.com/streaming/reference/get/statuses/firehose" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/statuses/firehose</a>
    *
    * @param count: Optional. The number of messages to backfill.
    *               For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#count" target="_blank">
    *                 https://dev.twitter.com/streaming/overview/request-parameters#count</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f: the function that defines how to process the received messages.
    */
  def getStatusesFirehouse(count: Option[Int] = None, stall_warnings: Boolean = false)(f: StreamingUpdate => Unit): Future[Unit] = {
    val maxCount = 150000
    require(Math.abs(count.getOrElse(0)) <= maxCount, s"count must be between -$maxCount and +$maxCount")
    val parameters = StatusFirehouseParameters(count, stall_warnings)
    val listener = createListener(f)
    streamingPipeline(listener, Get(firehoseUrl, parameters))
  }
}
