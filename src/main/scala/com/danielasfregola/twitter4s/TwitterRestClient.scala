package com.danielasfregola.twitter4s

import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
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
import com.danielasfregola.twitter4s.util.Configurations._

/** Represents the functionalities offered by the Twitter REST API
  */
class TwitterRestClient(val consumerToken: ConsumerToken, val accessToken: AccessToken) extends TwitterClients {

  protected val restClient = new RestClient(consumerToken, accessToken)

}

trait TwitterClients
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

object TwitterRestClient {

  def apply(): TwitterRestClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    apply(consumerToken, accessToken)
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestClient =
    new TwitterRestClient(consumerToken, accessToken)
}
