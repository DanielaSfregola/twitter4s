package twitter4s.http.clients.directmessages

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.DirectMessage
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterDirectMessageClientSpec extends ClientSpec {

  trait TwitterDirectMessageClientSpecContext extends ClientSpecContext with TwitterDirectMessageClient

  "Twitter Direct Message Client" should {

    "retrieve sent direct messages" in new TwitterDirectMessageClientSpecContext {
      val result: Seq[DirectMessage] = when(directMessagesSent(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/direct_messages/sent.json"
        request.uri.query === Query("count=10&include_entities=true&page=-1")
      }.respondWith("/twitter/direct_messages_sent.json").await
      result === loadJsonAs[Seq[DirectMessage]]("/fixtures/direct_messages_sent.json")
    }
  }
}
