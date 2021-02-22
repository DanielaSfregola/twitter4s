package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.enums.OAuthMode.{MixedAuth, OAuthMode, UseOAuth1, UseOAuthBearerToken}
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
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.util.{Configurations, SystemShutdown}

/** Represents the functionalities offered by the Twitter REST API
  */
class TwitterRestClient(val consumerToken: ConsumerToken,
                        val accessToken: AccessToken,
                        val bearerToken: String,
                        authMode: OAuthMode)(implicit _system: ActorSystem = ActorSystem("twitter4s-rest"))
    extends RestClients
    with SystemShutdown {
  protected val system = _system

  protected val restClient = new RestClient(authMode)
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
    // See what we have to work with
    authMode match {
      case UseOAuth1 => {
        val consumerToken = ConsumerToken(key = consumerTokenKey.get, secret = consumerTokenSecret.get)
        val accessToken = AccessToken(key = accessTokenKey.get, secret = accessTokenSecret.get)
        apply(consumerToken, accessToken)
      }
      case UseOAuthBearerToken => {
        apply(bearerToken = bearerToken.get)
      }
      case MixedAuth => {
        val consumerToken = ConsumerToken(key = consumerTokenKey.get, secret = consumerTokenSecret.get)
        val accessToken = AccessToken(key = accessTokenKey.get, secret = accessTokenSecret.get)
        apply(consumerToken, accessToken, bearerToken = bearerToken.get)
      }
    }
  }

  // Mixed constructor
  def apply(consumerToken: ConsumerToken, accessToken: AccessToken, bearerToken: String): TwitterRestClient =
    new TwitterRestClient(consumerToken, accessToken, bearerToken, MixedAuth)

  // OAuth1 constructor
  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestClient =
    new TwitterRestClient(consumerToken, accessToken, bearerToken = null, UseOAuth1)

  // Bearer token constructor
  def apply(bearerToken: String) = {
    new TwitterRestClient(null, null, bearerToken, UseOAuthBearerToken)
  }

  def withActorSystem(system: ActorSystem): TwitterRestClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey.getOrElse(""), secret = consumerTokenSecret.getOrElse(""))
    val accessToken = AccessToken(key = accessTokenKey.getOrElse(""), secret = accessTokenSecret.getOrElse(""))
    withActorSystem(consumerToken, accessToken, bearerToken.getOrElse(""), Configurations.authMode)(system)
  }

  def withActorSystem(consumerToken: ConsumerToken, accessToken: AccessToken, bearerToken: String, authMode: OAuthMode)(
      system: ActorSystem): TwitterRestClient =
    new TwitterRestClient(consumerToken, accessToken, bearerToken, authMode)(system)

}
