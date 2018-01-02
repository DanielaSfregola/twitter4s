package com.danielasfregola.twitter4s
package http.clients.streaming.statuses

import com.danielasfregola.twitter4s.entities.enums.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters._
import com.danielasfregola.twitter4s.http.clients.streaming.{StreamingClient, TwitterStream}
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

// https://github.com/krasserm/streamz/blob/master/streamz-converter/README.md


trait TwitterStatusClient {

  protected val streamingClient: StreamingClient

  private val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

  /** Starts a streaming connection from Twitter's public API, filtered with the 'follow', 'track' and 'location' parameters.
    * Although all of those three params are optional, at least one must be specified.
    * The track, follow, and locations fields should be considered to be combined with an OR operator.
    * The function returns a future of a `TwitterStream` that can be used to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `CommonStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>.
    *
    * @param follow : Empty by default. List of user IDs, indicating the users whose Tweets should be delivered on the stream.
    *                 For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
    *                   https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>
    * @param tracks : Empty by default. List of phrases which will be used to determine what Tweets will be delivered on the stream.
    *                 Each phrase must be between 1 and 60 bytes, inclusive.
    *                 For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
    *                  https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>
    * @param locations : Empty by default. Specifies a set of bounding boxes to track.
    *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
    *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>
    * @param languages : Empty by default. List of 'BCP 47' language identifiers.
    *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters" target="_blank">
    *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param filter_level : Default value is none, which includes all available Tweets.
    *                       Set the minimum value of the filter_level Tweet attribute required to be included in the stream.
    * @param f : Function that defines how to process the received messages.
    */
  def filterStatuses(follow: Seq[Long] = Seq.empty,
                     tracks: Seq[String] = Seq.empty,
                     locations: Seq[Double] = Seq.empty,
                     languages: Seq[Language] = Seq.empty,
                     stall_warnings: Boolean = false,
                     filter_level: FilterLevel = FilterLevel.None)(f: PartialFunction[CommonStreamingMessage, Unit]): Future[TwitterStream] = {
    import streamingClient._
    require(follow.nonEmpty || tracks.nonEmpty || locations.nonEmpty, "At least one of 'follow', 'tracks' or 'locations' needs to be non empty")
    val filters = StatusFilters(follow, tracks, locations, languages, stall_warnings, filter_level)
    preProcessing()
    Post(s"$statusUrl/filter.json", filters).processStream(f)
  }

  /** Starts a streaming connection from Twitter's public API, which is a a small random sample of all public statuses.
    * The Tweets returned by the default access level are the same, so if two different clients connect to this endpoint, they will see the same Tweets.
    * The function returns a future of a `TwitterStream` that can be used to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `CommonStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/tweets/sample-realtime/overview/GET_statuse_sample" target="_blank">
    *   https://developer.twitter.com/en/docs/tweets/sample-realtime/overview/GET_statuse_sample</a>.
    *
    * @param languages : Empty by default. List of 'BCP 47' language identifiers.
    *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters" target="_blank">
    *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param tracks : Empty by default. List of phrases which will be used to determine what Tweets will be delivered on the stream.
    *                 Each phrase must be between 1 and 60 bytes, inclusive.
    *                 For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
    *                  https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>
    * @param filter_level : Default value is none, which includes all available Tweets.
    *                       Set the minimum value of the filter_level Tweet attribute required to be included in the stream.
    * @param f : Function that defines how to process the received messages.
    */
  def sampleStatuses(languages: Seq[Language] = Seq.empty,
                     stall_warnings: Boolean = false,
                     tracks: Seq[String] = Seq.empty,
                     filter_level: FilterLevel = FilterLevel.None)
                    (f: PartialFunction[CommonStreamingMessage, Unit]): Future[TwitterStream] = {
    import streamingClient._
    val parameters = StatusSampleParameters(languages, stall_warnings, tracks, filter_level)
    preProcessing()
    Get(s"$statusUrl/sample.json", parameters).processStream(f)
  }

  /** Starts a streaming connection from Twitter's firehose API of all public statuses.
    * Few applications require this level of access.
    * Creative use of a combination of other resources and various access levels can satisfy nearly every application use case.
    * For more information see <a href="https://dev.twitter.com/streaming/reference/get/statuses/firehose" target="_blank">
    *   https://dev.twitter.com/streaming/reference/get/statuses/firehose</a>.
    * The function returns a future of a `TwitterStream` that can be used to close or replace the stream when needed.
    * If there are failures in establishing the initial connection, the Future returned will be completed with a failure.
    * Since it's an asynchronous event stream, all the events will be parsed as entities of type `CommonStreamingMessage`
    * and processed accordingly to the partial function `f`. All the messages that do not match `f` are automatically ignored.
    *
    * @param count : Optional. The number of messages to backfill.
    *               For more information see <a href="https://dev.twitter.com/streaming/overview/request-parameters#count" target="_blank">
    *                 https://dev.twitter.com/streaming/overview/request-parameters#count</a>
    * @param languages : Empty by default. List of 'BCP 47' language identifiers.
    *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters" target="_blank">
    *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters</a>
    * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
    * @param f : Function that defines how to process the received messages.
    */
  def firehoseStatuses(count: Option[Int] = None,
                       languages: Seq[Language] = Seq.empty,
                       stall_warnings: Boolean = false)
                      (f: PartialFunction[CommonStreamingMessage, Unit]): Future[TwitterStream] = {
    import streamingClient._
    val maxCount = 150000
    require(Math.abs(count.getOrElse(0)) <= maxCount, s"count must be between -$maxCount and +$maxCount")
    val parameters = StatusFirehoseParameters(languages, count, stall_warnings)
    preProcessing()
    Get(s"$statusUrl/firehose.json", parameters).processStream(f)
  }


  object FS2 {
    import cats.effect.IO
    import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

    /** Emits an FS2 Stream
      * 
      * 
      */
    def sampleStatusesStream(languages: Seq[Language] = Seq.empty,
      stall_warnings: Boolean = false,
      tracks: Seq[String] = Seq.empty,
      filter_level: FilterLevel = FilterLevel.None)
      (fs2Sink: fs2.Sink[IO, StreamingMessage]): Future[TwitterStream] = {
      import streamingClient._
      val parameters = StatusSampleParameters(languages, stall_warnings, tracks, filter_level)
      preProcessing()
      Get(s"$statusUrl/sample.json", parameters).processStreamFS2(fs2Sink)
    }
  



  }
}
