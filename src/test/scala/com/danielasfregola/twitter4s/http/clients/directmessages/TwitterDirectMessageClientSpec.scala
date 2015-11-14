package com.danielasfregola.twitter4s.http.clients.directmessages

import com.danielasfregola.twitter4s.http.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.DirectMessage
import com.danielasfregola.twitter4s.http.ClientSpecContext

class TwitterDirectMessageClientSpec extends ClientSpec {

  trait TwitterDirectMessageClientSpecContext extends ClientSpecContext with TwitterDirectMessageClient

  "Twitter Direct Message Client" should {

    "retrieve a specific direct message" in new TwitterDirectMessageClientSpecContext {
      val result: DirectMessage = when(directMessage(649298254383980547L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/show.json"
        request.uri.query === Query("id=649298254383980547")
      }.respondWith("/twitter/directmessages/show.json").await
      result === loadJsonAs[DirectMessage]("/fixtures/directmessages/show.json")
    }

    "get sent direct messages" in new TwitterDirectMessageClientSpecContext {
      val result: Seq[DirectMessage] = when(sentDirectMessages(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/sent.json"
        request.uri.query === Query("count=10&include_entities=true&page=-1")
      }.respondWith("/twitter/directmessages/sent.json").await
      result === loadJsonAs[Seq[DirectMessage]]("/fixtures/directmessages/sent.json")
    }

    "get received direct messages" in new TwitterDirectMessageClientSpecContext {
      val result: Seq[DirectMessage] = when(receivedDirectMessages(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages.json"
        request.uri.query === Query("count=10&include_entities=true&skip_status=false")
      }.respondWith("/twitter/directmessages/received.json").await
      result === loadJsonAs[Seq[DirectMessage]]("/fixtures/directmessages/received.json")
    }

    "destroy a direct message" in new TwitterDirectMessageClientSpecContext {
      val result: DirectMessage = when(deleteDirectMessage(649298254383980547L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/destroy.json"
        request.uri.query === Query("id=649298254383980547&include_entities=true")
      }.respondWith("/twitter/directmessages/destroy.json").await
      result === loadJsonAs[DirectMessage]("/fixtures/directmessages/destroy.json")
    }

    "create a direct message by user_id" in new TwitterDirectMessageClientSpecContext {
      val text = "FUNZIONAAAAAAAAAA :D"
      val result: DirectMessage = when(createDirectMessage(2911461333L, text)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/new.json"
        request.uri.query === Query("text=FUNZIONAAAAAAAAAA+:D&user_id=2911461333")
      }.respondWith("/twitter/directmessages/new.json").await
      result === loadJsonAs[DirectMessage]("/fixtures/directmessages/new.json")
    }

    "create a direct message by screen_name" in new TwitterDirectMessageClientSpecContext {
      val text = "FUNZIONAAAAAAAAAA :D"
      val result: DirectMessage = when(createDirectMessage("marcobonzanini", text)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/new.json"
        request.uri.query === Query("screen_name=marcobonzanini&text=FUNZIONAAAAAAAAAA+:D")
      }.respondWith("/twitter/directmessages/new.json").await
      result === loadJsonAs[DirectMessage]("/fixtures/directmessages/new.json")
    }

  }


}
