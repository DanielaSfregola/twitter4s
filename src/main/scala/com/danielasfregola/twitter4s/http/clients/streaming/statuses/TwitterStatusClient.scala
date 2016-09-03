package com.danielasfregola.twitter4s.http.clients.streaming.statuses

import akka.actor.ActorRef
import com.danielasfregola.twitter4s.http.clients.StreamingOAuthClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters.{StatusFilterParameters, StatusSampleParameters}
import com.danielasfregola.twitter4s.util.{ActorContextExtractor, Configurations}
import com.danielasfregola.twitter4s.listeners.TwitterStreamListener

import scala.concurrent.Future
import scala.reflect.ClassTag

trait TwitterStatusClient extends StreamingOAuthClient with Configurations with ActorContextExtractor {

  private[twitter4s] def attachListener[Listener <: TwitterStreamListener : ClassTag]: ActorRef

  private val filterUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/filter.json"
  private val sampleUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/sample.json"

  /** Starts a streaming connection from Twitter's public API, filtered with the 'follow', 'track' and 'location' parameters.
    * Although all of those three params are optional, at least one must be specified.
    * The function only returns an empty future, that can be used to track failures in establishing the initial connection.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `StreamingUpdate[StreamingEvent]`
    * and forwarded to the instance of [[com.danielasfregola.twitter4s.listeners.TwitterStreamListener]]
    * provided to the [[com.danielasfregola.twitter4s.TwitterStreamingClient]].
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/post/statuses/filter" target="_blank">
    *   https://dev.twitter.com/streaming/reference/post/statuses/filter</a>.
    * Note: delimited is, for now, not supported
    *
    * @param follow : Optional, A comma separated list of user IDs, indicating the users to return statuses for in the stream.
    * @param track : Optional, Keywords to track. Phrases of keywords are specified by a comma-separated list.
    * @param locations : Optional, Specifies a set of bounding boxes to track.
    * @param stall_warnings : Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    */
  def getStatusesFilter[Listener <: TwitterStreamListener: ClassTag](follow: Option[String] = None,
                                                                     track: Option[String] = None,
                                                                     locations: Option[String] = None,
                                                                     stall_warnings: Boolean = false): Future[Unit] = {
    require(follow.orElse(track).orElse(locations).isDefined, "At least one of 'follow', 'track' or 'locations' needs to be defined")
    val parameters = StatusFilterParameters(follow, track, locations, stall_warnings)
    val listener = attachListener[Listener]
    streamingPipeline(listener, Get(filterUrl, parameters))
  }

  /** Same as getStatusesFilter, both GET and POST requests are supported,
    * but GET requests with too many parameters may cause the request to be rejected for excessive URL length.
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/post/statuses/filter" target="_blank">
    *   https://dev.twitter.com/streaming/reference/post/statuses/filter</a>.
    * Note: delimited is, for now, not supported
    *
    * @param follow : Optional, A comma separated list of user IDs, indicating the users to return statuses for in the stream.
    * @param track : Optional, Keywords to track. Phrases of keywords are specified by a comma-separated list.
    * @param locations : Optional, Specifies a set of bounding boxes to track.
    * @param stall_warnings : Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    */
  def postStatusesFilter[Listener <: TwitterStreamListener: ClassTag](follow: Option[String] = None,
                                                                      track: Option[String] = None,
                                                                      locations: Option[String] = None,
                                                                      stall_warnings: Boolean = false): Future[Unit] = {
    require(follow.orElse(track).orElse(locations).isDefined, "At least one of 'follow', 'track' or 'locations' needs to be defined")
    val parameters = StatusFilterParameters(follow, track, locations)
    val listener = attachListener[Listener]
    streamingPipeline(listener, Post(filterUrl, parameters.asInstanceOf[Product]))
  }

  /** Starts a streaming connection from Twitter's public API, which is a a small random sample of all public statuses.
    * The Tweets returned by the default access level are the same, so if two different clients connect to this endpoint, they will see the same Tweets.
    * The function only returns an empty future, that can be used to track failures in establishing the initial connection.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `StreamingUpdate[StreamingEvent]`
    * and forwarded to the instance of [[com.danielasfregola.twitter4s.listeners.TwitterStreamListener]]
    * provided to the [[com.danielasfregola.twitter4s.TwitterStreamingClient]].
    * For more information see
    * <a href="https://dev.twitter.com/streaming/reference/get/statuses/sample" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/statuses/sample</a>.
    * Note: delimited is, for now, not supported
    *
    * @param stall_warnings : Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    */
  def getStatusesSample[Listener <: TwitterStreamListener: ClassTag](stall_warnings: Boolean = false): Future[Unit] = {
    val parameters = StatusSampleParameters(stall_warnings)
    val listener = attachListener[Listener]
    streamingPipeline(listener, Get(sampleUrl, parameters))
  }
}
