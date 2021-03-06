package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, BearerToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.account.TwitterAccountClient
import com.danielasfregola.twitter4s.http.clients.rest.accountactivity.TwitterAccountActivityClient
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
import com.danielasfregola.twitter4s.http.oauth.AuthClient
import com.danielasfregola.twitter4s.util.SystemShutdown

/** Represents the functionalities offered by the Twitter REST API
  */
class TwitterRestClient(authClient: AuthClient)(implicit _system: ActorSystem = ActorSystem("twitter4s-rest"))
    extends RestClients
    with SystemShutdown {
  override protected def system: ActorSystem = _system

  override protected val restClient: RestClient = new RestClient(authClient)
}

trait RestClients
    extends TwitterAccountClient
    with TwitterAccountActivityClient
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

object TwitterRestClient {

  def apply(): TwitterRestClient = {
    new TwitterRestClient(AuthClient())
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestClient = {
    val authClient = AuthClient.oauth(consumerToken, accessToken)
    new TwitterRestClient(authClient)
  }

  def apply(bearerToken: BearerToken): TwitterRestClient = {
    val authClient = AuthClient.bearer(bearerToken)
    new TwitterRestClient(authClient)
  }

  def withActorSystem(system: ActorSystem): TwitterRestClient = {
    new TwitterRestClient(AuthClient())(system)
  }

  def withActorSystem(consumerToken: ConsumerToken, accessToken: AccessToken)(
      system: ActorSystem): TwitterRestClient = {
    val authClient = AuthClient.oauth(consumerToken, accessToken)
    new TwitterRestClient(authClient)(system)
  }

  def withActorSystem(bearerToken: BearerToken)(system: ActorSystem): TwitterRestClient = {
    val authClient = AuthClient.bearer(bearerToken)
    new TwitterRestClient(authClient)(system)
  }

}
