package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
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
import com.danielasfregola.twitter4s.http.clients.{BearerAuthClient, OAuthClient}
import com.danielasfregola.twitter4s.util.Configurations.{
  accessTokenKey,
  accessTokenSecret,
  consumerTokenKey,
  consumerTokenSecret,
  _
}
import com.danielasfregola.twitter4s.util.SystemShutdown

/** Represents the functionalities offered by the Twitter REST API
  */
class TwitterRestClient private (val OAuthClient: Option[OAuthClient] = None,
                                 val bearerTokenClient: Option[BearerAuthClient] = None)(
    implicit _system: ActorSystem = ActorSystem("twitter4s-rest"))
    extends RestClients
    with SystemShutdown {
  override protected def system: ActorSystem = _system

  override protected val restClient: RestClient = new RestClient(OAuthClient, bearerTokenClient)
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
    var OAuthClient: Option[OAuthClient] = None
    var btClient: Option[BearerAuthClient] = None

    // See if we can define an OAuth client
    (consumerTokenKey, consumerTokenSecret, accessTokenKey, accessTokenSecret) match {
      case (Some(consumerTokenKey), Some(consumerTokenSecret), Some(accessTokenKey), Some(accessTokenSecret)) =>
        OAuthClient = Some(
          new OAuthClient(ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret),
                          AccessToken(key = accessTokenKey, secret = accessTokenSecret)))
      case _ => OAuthClient = None
    }

    // See if we can define a bearer token client
    (bearerToken) match {
      case (Some(bearerToken)) => btClient = Some(new BearerAuthClient(bearerToken))
      case _                   => btClient = None
    }

    new TwitterRestClient(OAuthClient, btClient)
  }

  // Mixed constructor
  def apply(consumerToken: ConsumerToken, accessToken: AccessToken, bearerToken: String): TwitterRestClient =
    new TwitterRestClient(Some(new OAuthClient(consumerToken, accessToken)), Some(new BearerAuthClient(bearerToken)))

  // OAuth1 constructor
  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestClient =
    new TwitterRestClient(Some(new OAuthClient(consumerToken, accessToken)))

  // Bearer token constructor
  def apply(bearerToken: String) =
    new TwitterRestClient(bearerTokenClient = Some(new BearerAuthClient(bearerToken)))

  def withActorSystem(system: ActorSystem): TwitterRestClient = {
    // This is like calling the default constructor, need to determine what we have:
    new TwitterRestClient()(system)
  }
}
