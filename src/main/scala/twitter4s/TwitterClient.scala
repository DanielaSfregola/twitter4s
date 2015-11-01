package twitter4s

import akka.actor.{ActorRefFactory, ActorSystem}

import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.account.TwitterAccountClient
import twitter4s.http.clients.application.TwitterApplicationClient
import twitter4s.http.clients.blocks.TwitterBlockClient
import twitter4s.http.clients.directmessages.TwitterDirectMessageClient
import twitter4s.http.clients.favorites.TwitterFavoriteClient
import twitter4s.http.clients.followers.TwitterFollowerClient
import twitter4s.http.clients.friends.TwitterFriendClient
import twitter4s.http.clients.friendships.TwitterFriendshipClient
import twitter4s.http.clients.geo.TwitterGeoClient
import twitter4s.http.clients.help.TwitterHelpClient
import twitter4s.http.clients.media.TwitterMediaClient
import twitter4s.http.clients.mutes.TwitterMuteClient
import twitter4s.http.clients.savedsearches.TwitterSavedSearchClient
import twitter4s.http.clients.search.TwitterSearchClient
import twitter4s.http.clients.statuses.TwitterStatusClient
import twitter4s.http.clients.lists.TwitterListClient
import twitter4s.http.clients.suggestions.TwitterSuggestionClient
import twitter4s.http.clients.trends.TwitterTrendClient
import twitter4s.http.clients.users.TwitterUserClient

class TwitterClient(val consumerToken: ConsumerToken = ConsumerTokenFromConf, val accessToken: AccessToken = AccessTokenFromConf)
                   (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends OAuthClient with Clients

trait Clients extends TwitterStatusClient
  with TwitterDirectMessageClient
  with TwitterFriendshipClient
  with TwitterFollowerClient
  with TwitterFriendClient
  with TwitterAccountClient
  with TwitterBlockClient
  with TwitterUserClient
  with TwitterMuteClient
  with TwitterSuggestionClient
  with TwitterFavoriteClient
  with TwitterListClient
  with TwitterGeoClient
  with TwitterTrendClient
  with TwitterApplicationClient
  with TwitterHelpClient
  with TwitterSearchClient
  with TwitterSavedSearchClient
  with TwitterMediaClient
