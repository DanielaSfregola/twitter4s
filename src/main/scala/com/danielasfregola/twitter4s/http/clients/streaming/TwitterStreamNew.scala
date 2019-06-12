package com.danielasfregola.twitter4s.http.clients.streaming
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.scaladsl.Source
import akka.stream.{KillSwitches, SharedKillSwitch}
import com.danielasfregola.twitter4s.entities.GeoBoundingBox.toLngLatPairs
import com.danielasfregola.twitter4s.entities.enums.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, GeoBoundingBox}
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters.StatusFilters
import com.danielasfregola.twitter4s.util.Configurations.{statusStreamingTwitterUrl, twitterVersion}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

class TwitterStreamNew(consumerToken: ConsumerToken, accessToken: AccessToken)(private val request: HttpRequest,
                                                                               private val system: ActorSystem)
    extends LazyLogging {

  private val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

  protected val streamingClient = new StreamingClientNew(consumerToken, accessToken)(system)

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
  }

}
