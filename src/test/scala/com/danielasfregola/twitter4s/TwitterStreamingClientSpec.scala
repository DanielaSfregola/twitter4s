package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.AwaitableFuture
import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class TwitterStreamingClientSpec extends Specification with Mockito with AwaitableFuture {
  
  class SpecContext extends Scope {
    val system = ActorSystem()
  }
  
  "TwitterStreamingClient" should {

    "be created from no params" in new SpecContext {
      val client = TwitterStreamingClient() 
      
      client should not(beNull)
      client.consumerToken === ConsumerToken("my-consumer-key", "my-consumer-secret")
      client.accessToken === AccessToken("my-access-key", "my-access-secret")
    }


    "be created from a consumer token and an access token" in new SpecContext {
      val consumerToken = mock[ConsumerToken]
      val accessToken = mock[AccessToken]
      
      val client = TwitterStreamingClient(consumerToken, accessToken) 
      
      client should not(beNull)
      client.consumerToken === consumerToken
      client.accessToken === accessToken
    }
  }
}
