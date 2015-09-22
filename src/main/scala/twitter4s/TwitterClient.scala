package twitter4s

import akka.actor.{ActorRefFactory, ActorSystem}

import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.clients.OAuthClient
import twitter4s.statuses.TwitterStatusClient

class TwitterClient(consumerToken: ConsumerToken = ConsumerTokenFromConf,
                    accessToken: AccessToken = AccessTokenFromConf)
                   (implicit actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends OAuthClient(consumerToken, accessToken)
                    with TwitterStatusClient

