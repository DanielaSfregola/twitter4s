package com.danielasfregola.twitter4s.http.clients.rest.accountactivity

import akka.http.scaladsl.model._
import com.danielasfregola.twitter4s.entities.Webhook
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterAccountActivityClientSpec extends ClientSpec {

  class TwitterAccountClientSpecContext extends RestClientSpecContext with TwitterAccountActivityClient

  "Twitter Account Activity Client" should {

    "register webhook" in new TwitterAccountClientSpecContext {
      val result: Webhook = when(registerWebhook(env_name = "test", url = "https://danielasfregola.com/webhook"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account_activity/all/test/webhooks.json"
          request.uri.rawQueryString === Some("url=https%3A%2F%2Fdanielasfregola.com%2Fwebhook")
        }
        .respondWith("/twitter/rest/accountactivity/webhook.json")
        .await
      result === loadJsonAs[Webhook]("/fixtures/rest/accountactivity/webhook.json")
    }

    "remove webhook" in new TwitterAccountClientSpecContext {
      val result: Unit = when(removeWebhook(env_name = "test", webhookId = "1234567890123456789"))
        .expectRequest { request =>
          request.method === HttpMethods.DELETE
          request.uri.endpoint === "https://api.twitter.com/1.1/account_activity/all/test/webhooks/1234567890123456789.json"
        }
        .respondWithNoContent
        .await
      result.isInstanceOf[Unit] should beTrue
    }

    "reenable webhook" in new TwitterAccountClientSpecContext {
      val result: Unit = when(reenableWebhook(env_name = "test", webhookId = "1234567890123456789"))
        .expectRequest { request =>
          request.method === HttpMethods.PUT
          request.uri.endpoint === "https://api.twitter.com/1.1/account_activity/all/test/webhooks/1234567890123456789.json"
        }
        .respondWithNoContent
        .await
      result.isInstanceOf[Unit] should beTrue
    }

    "determine if user is subscribed" in new TwitterAccountClientSpecContext {
      val result: Unit = when(isUserSubscribed(env_name = "test"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/account_activity/all/test/subscriptions.json"
        }
        .respondWithNoContent
        .await
      result.isInstanceOf[Unit] should beTrue
    }

    "subscribe context user to all events" in new TwitterAccountClientSpecContext {
      val result: Unit = when(subscribeAll(env_name = "test"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/account_activity/all/test/subscriptions.json"
        }
        .respondWithNoContent
        .await
      result.isInstanceOf[Unit] should beTrue
    }
  }

}
