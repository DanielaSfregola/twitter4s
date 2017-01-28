package com.danielasfregola.twitter4s

import java.util.UUID

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.rest.account.TwitterAccountClient
import com.danielasfregola.twitter4s.http.clients.rest.application.TwitterApplicationClient
import com.danielasfregola.twitter4s.http.clients.rest.blocks.TwitterBlockClient
import com.danielasfregola.twitter4s.http.clients.rest.directmessages.TwitterDirectMessageClient
import com.danielasfregola.twitter4s.http.clients.rest.favorites.TwitterFavoriteClient
import com.danielasfregola.twitter4s.http.clients.rest.followers.TwitterFollowerClient
import com.danielasfregola.twitter4s.http.clients.rest.friends.TwitterFriendClient
import com.danielasfregola.twitter4s.http.clients.rest.friendships.TwitterFriendshipClient
import com.danielasfregola.twitter4s.http.clients.rest.geo.TwitterGeoClient
import com.danielasfregola.twitter4s.http.clients.rest.help.TwitterHelpClient
import com.danielasfregola.twitter4s.http.clients.rest.lists.TwitterListClient
import com.danielasfregola.twitter4s.http.clients.rest.media.TwitterMediaClient
import com.danielasfregola.twitter4s.http.clients.rest.mutes.TwitterMuteClient
import com.danielasfregola.twitter4s.http.clients.rest.savedsearches.TwitterSavedSearchClient
import com.danielasfregola.twitter4s.http.clients.rest.search.TwitterSearchClient
import com.danielasfregola.twitter4s.http.clients.rest.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.rest.suggestions.TwitterSuggestionClient
import com.danielasfregola.twitter4s.http.clients.rest.trends.TwitterTrendClient
import com.danielasfregola.twitter4s.http.clients.rest.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.Configurations

import scala.concurrent.Future

class TwitterRestClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                       (val system: ActorSystem) extends RestClients {

  /** Terminates the actor system associated to the client.
    *
    * @return : Future that will be completed with Unit once the system has been shut down.
    * */
  def close(): Future[Unit] = system.terminate.map(_ => (): Unit)

}

trait RestClients
    extends TwitterAccountClient
    with TwitterApplicationClient
    with TwitterBlockClient
    with TwitterDirectMessageClient
    with TwitterFavoriteClient
    with TwitterFollowerClient
    with TwitterFriendClient
    with TwitterFriendshipClient
    with TwitterGeoClient
    with TwitterHelpClient
    with TwitterListClient
    with TwitterMediaClient
    with TwitterMuteClient
    with TwitterSavedSearchClient
    with TwitterSearchClient
    with TwitterStatusClient
    with TwitterSuggestionClient
    with TwitterTrendClient
    with TwitterUserClient

object TwitterRestClient extends Configurations {

  def apply(system: ActorSystem = ActorSystem(s"twitter4s-rest-${UUID.randomUUID}")): TwitterRestClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    apply(consumerToken, accessToken, system)
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestClient =
    apply(consumerToken, accessToken, ActorSystem(s"twitter4s-rest-${UUID.randomUUID}"))

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken, system: ActorSystem): TwitterRestClient =
    new TwitterRestClient(consumerToken, accessToken)(system)
}
