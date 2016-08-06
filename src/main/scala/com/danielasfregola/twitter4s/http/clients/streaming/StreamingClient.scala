package com.danielasfregola.twitter4s.http.clients.streaming

import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.streaming.parameters.StatusFilterParameters
import com.danielasfregola.twitter4s.util.{ActorContextExtractor, Configurations}

import akka.actor.ActorRef


import scala.concurrent.Future

/**
 * Created by gerardo.mendez on 8/3/16.
 */

trait TwitterStreamingClient extends OAuthClient with Configurations with ActorContextExtractor {

  private val filterUrl = s"$streamingTwitterUrl/$twitterVersion/statuses/filter.json"

  def getStatusesFilter(follow: Option[String] = None,
                        track: Option[String] = None,
                        locations: Option[String] = None)(implicit self: ActorRef): Future[Unit] = {

    val parameters = StatusFilterParameters(follow, track, locations)
    Get(filterUrl, parameters).respondAsStreamOf[Tweet](self)
  }
}
