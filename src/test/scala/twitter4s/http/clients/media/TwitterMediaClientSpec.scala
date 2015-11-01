package twitter4s.http.clients.media

import twitter4s.util.{ClientSpecContext, ClientSpec}

class TwitterMediaClientSpec extends ClientSpec {

  trait TwitterMediaClientSpecContext extends ClientSpecContext with TwitterMediaClient

  "Twitter Media Client" should {

    "upload a media file" in new TwitterMediaClientSpecContext { failure }
  }


}
