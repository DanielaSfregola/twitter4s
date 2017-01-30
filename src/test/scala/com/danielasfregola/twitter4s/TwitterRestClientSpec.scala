package com.danielasfregola.twitter4s

import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.AwaitableFuture
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class TwitterRestClientSpec extends Specification with Mockito with AwaitableFuture {
  
  "TwitterRestClient" should {

    "be created from no params" in {
      val client = TwitterRestClient() 
      
      client should not(beNull)
      client.consumerToken === ConsumerToken("my-consumer-key", "my-consumer-secret")
      client.accessToken === AccessToken("my-access-key", "my-access-secret")
    }

    "be created from a consumer token and an access token" in {
      val consumerToken = mock[ConsumerToken]
      val accessToken = mock[AccessToken]
      
      val client = TwitterRestClient(consumerToken, accessToken) 
      
      client should not(beNull)
      client.consumerToken === consumerToken
      client.accessToken === accessToken
    }
  }
}
