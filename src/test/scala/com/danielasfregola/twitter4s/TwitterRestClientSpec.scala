package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.AwaitableFuture
import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class TwitterRestClientSpec extends Specification with Mockito with AwaitableFuture {
  
  class SpecContext extends Scope {
    val system = ActorSystem()
  }
  
  "TwitterRestClient" should {

    "be created from no params" in new SpecContext {
      val client = TwitterRestClient() 
      
      client should not(beNull)
      client.system.name should startingWith("twitter4s-rest")
    }

    "be created from an actor system" in new SpecContext {
      val client = TwitterRestClient(system) 
      
      client should not(beNull)
      client.system === system
    }


    "be created from a consumer token and an access token" in new SpecContext {
      val consumerToken = mock[ConsumerToken]
      val accessToken = mock[AccessToken]
      
      val client = TwitterRestClient(consumerToken, accessToken) 
      
      client should not(beNull)
      client.system.name should startingWith("twitter4s-rest")
    }

    "be created from a consumer token, an access token and an actor system" in new SpecContext {
      val consumerToken = mock[ConsumerToken]
      val accessToken = mock[AccessToken]
      
      val client = TwitterRestClient(consumerToken, accessToken, system)
      
      client should not(beNull)
      client.system === system
    }

    "be closable" in new SpecContext {
      val client = TwitterRestClient(system)

      val result = client.close()

      result.await should not(throwAn[Exception])
      system.whenTerminated.await should not(throwAn[Exception])
    }
  }
}
