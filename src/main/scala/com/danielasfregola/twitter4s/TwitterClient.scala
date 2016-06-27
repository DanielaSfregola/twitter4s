package com.danielasfregola.twitter4s

import akka.actor.{ActorRefFactory, ActorSystem}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.account.TwitterAccountClient
import com.danielasfregola.twitter4s.http.clients.application.TwitterApplicationClient
import com.danielasfregola.twitter4s.http.clients.blocks.TwitterBlockClient
import com.danielasfregola.twitter4s.http.clients.directmessages.TwitterDirectMessageClient
import com.danielasfregola.twitter4s.http.clients.favorites.TwitterFavoriteClient
import com.danielasfregola.twitter4s.http.clients.followers.TwitterFollowerClient
import com.danielasfregola.twitter4s.http.clients.friends.TwitterFriendClient
import com.danielasfregola.twitter4s.http.clients.friendships.TwitterFriendshipClient
import com.danielasfregola.twitter4s.http.clients.geo.TwitterGeoClient
import com.danielasfregola.twitter4s.http.clients.help.TwitterHelpClient
import com.danielasfregola.twitter4s.http.clients.lists.TwitterListClient
import com.danielasfregola.twitter4s.http.clients.media.TwitterMediaClient
import com.danielasfregola.twitter4s.http.clients.mutes.TwitterMuteClient
import com.danielasfregola.twitter4s.http.clients.savedsearches.TwitterSavedSearchClient
import com.danielasfregola.twitter4s.http.clients.search.TwitterSearchClient
import com.danielasfregola.twitter4s.http.clients.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.suggestions.TwitterSuggestionClient
import com.danielasfregola.twitter4s.http.clients.trends.TwitterTrendClient
import com.danielasfregola.twitter4s.http.clients.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.TokensFromConfig

class TwitterClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                   (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends Clients

trait Clients extends TwitterAccountClient
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
  with TwitterUserClient
  with TwitterTrendClient

object TwitterClient {

  def apply(): TwitterClient = {
    val consumerToken = ConsumerToken(key = TokensFromConfig.consumerTokenKey, secret = TokensFromConfig.consumerTokenSecret)
    val accessToken = AccessToken(key = TokensFromConfig.accessTokenKey, secret = TokensFromConfig.accessTokenSecret)
    new TwitterClient(consumerToken, accessToken)
  }
}
