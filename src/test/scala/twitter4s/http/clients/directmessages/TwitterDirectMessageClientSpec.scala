package twitter4s.http.clients.directmessages

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.DirectMessage
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterDirectMessageClientSpec extends ClientSpec {

  trait TwitterDirectMessageClientSpecContext extends ClientSpecContext with TwitterDirectMessageClient

  "Twitter Direct Message Client" should {

    "retrieve a specific direct message" in new TwitterDirectMessageClientSpecContext {
      val id = 649298254383980547L
      val result: DirectMessage = when(directMessage(id)).expectRequest { request =>
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

  }


}
