package com.danielasfregola.twitter4s.http.clients.rest.directmessages

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{DirectMessage, RatedData}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterDirectMessageClientSpec extends ClientSpec {

  class TwitterDirectMessageClientSpecContext extends RestClientSpecContext with TwitterDirectMessageClient

  "Twitter Direct Message Client" should {

    "retrieve a specific direct message" in new TwitterDirectMessageClientSpecContext {
      val result: RatedData[DirectMessage] = when(directMessage(649298254383980547L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/show.json"
          request.uri.rawQueryString === Some("id=649298254383980547")
        }
        .respondWithRated("/twitter/rest/directmessages/show.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[DirectMessage]("/fixtures/rest/directmessages/show.json")
    }

    "get sent direct messages" in new TwitterDirectMessageClientSpecContext {
      val result: RatedData[Seq[DirectMessage]] = when(sentDirectMessages(count = 10))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/sent.json"
          request.uri.rawQueryString === Some("count=10&include_entities=true&page=-1")
        }
        .respondWithRated("/twitter/rest/directmessages/sent.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[DirectMessage]]("/fixtures/rest/directmessages/sent.json")
    }

    "get received direct messages" in new TwitterDirectMessageClientSpecContext {
      val result: RatedData[Seq[DirectMessage]] = when(receivedDirectMessages(count = 10))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages.json"
          request.uri.rawQueryString === Some("count=10&include_entities=true&skip_status=false")
        }
        .respondWithRated("/twitter/rest/directmessages/received.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[DirectMessage]]("/fixtures/rest/directmessages/received.json")
    }

    "destroy a direct message" in new TwitterDirectMessageClientSpecContext {
      val result: DirectMessage = when(deleteDirectMessage(649298254383980547L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/destroy.json"
          request.uri.rawQueryString === Some("id=649298254383980547&include_entities=true")
        }
        .respondWith("/twitter/rest/directmessages/destroy.json")
        .await
      result === loadJsonAs[DirectMessage]("/fixtures/rest/directmessages/destroy.json")
    }

    "create a direct message by user_id" in new TwitterDirectMessageClientSpecContext {
      val text = "FUNZIONAAAAAAAAAA :D"
      val result: DirectMessage = when(createDirectMessage(2911461333L, text))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/new.json"
          request.uri.rawQueryString === Some("text=FUNZIONAAAAAAAAAA%20%3AD&user_id=2911461333")
        }
        .respondWith("/twitter/rest/directmessages/new.json")
        .await
      result === loadJsonAs[DirectMessage]("/fixtures/rest/directmessages/new.json")
    }

    "create a direct message by screen_name" in new TwitterDirectMessageClientSpecContext {
      val text = "FUNZIONAAAAAAAAAA :D"
      val result: DirectMessage = when(createDirectMessage("marcobonzanini", text))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/new.json"
          request.uri.rawQueryString === Some("screen_name=marcobonzanini&text=FUNZIONAAAAAAAAAA%20%3AD")
        }
        .respondWith("/twitter/rest/directmessages/new.json")
        .await
      result === loadJsonAs[DirectMessage]("/fixtures/rest/directmessages/new.json")
    }

  }

}
