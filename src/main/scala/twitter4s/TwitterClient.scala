package twitter4s

import akka.actor.ActorRefFactory

import twitter4s.client.Client
import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}

class TwitterClient(val consumerToken: ConsumerToken = ConsumerTokenFromConf, val accessToken: AccessToken = AccessTokenFromConf)
                   (implicit val actorRefFactory: ActorRefFactory) extends Client {


}

