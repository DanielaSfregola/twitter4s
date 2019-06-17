package com.danielasfregola.twitter4s.http.clients.streaming
import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.typesafe.scalalogging.LazyLogging

class TwitterStreamNew(consumerToken: ConsumerToken, accessToken: AccessToken)(private val request: HttpRequest,
                                                                               private val system: ActorSystem)
    extends LazyLogging {

//  private val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

  /*protected val streamingClient = new StreamingClientNew(consumerToken, accessToken)(system)

  def filterStatuses(follow: Seq[Long] = Seq.empty,
                     tracks: Seq[String] = Seq.empty,
                     locations: Seq[GeoBoundingBox] = Seq.empty,
                     languages: Seq[Language] = Seq.empty,
                     stall_warnings: Boolean = false,
                     filter_level: FilterLevel = FilterLevel.None): Future[Source[StreamingMessage, NotUsed]] = {
    import streamingClient._
    require(follow.nonEmpty || tracks.nonEmpty || locations.nonEmpty,
            "At least one of 'follow', 'tracks' or 'locations' needs to be non empty")
    val filters = StatusFilters(follow, tracks, toLngLatPairs(locations), languages, stall_warnings, filter_level)
    preProcessing()
    val killSwitch: SharedKillSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")
    streamingClient.getStream(Post(s"$statusUrl/filter.json", filters), killSwitch)
  }*/

}
