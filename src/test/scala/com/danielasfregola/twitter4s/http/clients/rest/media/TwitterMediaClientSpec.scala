package com.danielasfregola.twitter4s.http.clients.rest.media

import akka.http.scaladsl.model._
import com.danielasfregola.twitter4s.entities.MediaDetails
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterMediaClientSpec extends ClientSpec {

  class TwitterMediaClientSpecContext extends RestClientSpecContext with TwitterMediaClient

  "Twitter Media Client" should {

    "upload a media" in new TwitterMediaClientSpecContext {
      val initMediaResponse = {
        val json =
          """{"media_id":925706675478151168,"media_id_string":"925706675478151168","expires_after_secs":86400}"""
        HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, json))
      }
      val imgPath = getClass.getResource("/imgs/twitter.png").getPath
      val result: MediaDetails =
        when(uploadMediaFromPath(imgPath))
          .expectRequest { request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/upload.json"
            request.uri.rawQueryString === Some("command=INIT&media_type=image%2Fpng&total_bytes=22689")
          }
          .respondWith(initMediaResponse)
          .expectRequest { request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/upload.json"
            request.entity.contentType.mediaType.isMultipart === true
          }
          .respondWith("/fixtures/rest/media/img_media_details.json")
          .expectRequest { request =>
            request.method === HttpMethods.POST
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/upload.json"
            request.uri.rawQueryString === Some("command=FINALIZE&media_id=925706675478151168")
          }
          .respondWith("/fixtures/rest/media/img_media_details.json")
          .await
      result === loadJsonAs[MediaDetails]("/fixtures/rest/media/img_media_details.json")
    }

    "check the status of a media upload" in new TwitterMediaClientSpecContext {
      val result: MediaDetails =
        when(statusMedia(media_id = 710511363345354753L))
          .expectRequest { request =>
            request.method === HttpMethods.GET
            request.uri.endpoint === "https://upload.twitter.com/1.1/media/upload.json"
            request.uri.rawQueryString === Some("command=STATUS&media_id=710511363345354753")
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
