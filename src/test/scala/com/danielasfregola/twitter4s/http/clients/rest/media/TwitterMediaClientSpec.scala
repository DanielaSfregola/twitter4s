package com.danielasfregola.twitter4s.http.clients.rest.media

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods}
import com.danielasfregola.twitter4s.entities.MediaDetails
import com.danielasfregola.twitter4s.util.rest.ClientSpec

class TwitterMediaClientSpec extends ClientSpec {

  class TwitterMediaClientSpecContext extends ClientSpecContext with TwitterMediaClient

  "Twitter Media Client" should {

    // TODO - test actual media upload?

    "check the status of a media upload" in new TwitterMediaClientSpecContext {
      val result: MediaDetails =
        when(statusMedia(media_id = 710511363345354753L))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/upload.json"
            request.uri.queryString() === Some("command=STATUS&media_id=710511363345354753")
          }
          .respondWith("/twitter/rest/media/media_details.json")
          .await
      result === loadJsonAs[MediaDetails]("/fixtures/rest/media/media_details.json")
    }

    "upload a media description" in new TwitterMediaClientSpecContext {
      val result: Unit =
        when(createMediaDescription(media_id = 710511363345354753L, description = "A cat picture"))
          .expectRequest { request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/metadata/create.json"
            request.entity === HttpEntity(ContentTypes.`application/json`,
                                          """{"media_id":"710511363345354753","description":"A cat picture"}""")
          }
          .respondWithOk
          .await
      result.isInstanceOf[Unit] should beTrue
    }
  }
}
