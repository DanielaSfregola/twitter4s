package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import com.danielasfregola.twitter4s.util.ClientSpec

import scala.concurrent.Future
import scala.util.{Failure, Success}
import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterError, TwitterException}

class OAuthClientSpec extends ClientSpec {

  class OAuthClientSpecContext extends ClientSpecContext with OAuthClient {

    def exampleRequest(): Future[Unit] = Get("an-example-request", ContentTypes.`application/json`).respondAs[Unit]
  }

  "OAuth Client" should {

    "throw twitter exception to twitter rejection" in new OAuthClientSpecContext {
      val response = {
        val entity = """{"errors":[{"message":"Sorry, that page does not exist","code":34}]}"""
        HttpResponse(StatusCodes.NotFound, entity = entity)
      }
      val result = when(exampleRequest).expectRequest(identity(_)).respondWith(response)
      val expectedTwitterException = TwitterException(code = StatusCodes.NotFound,
                                       errors = Errors(TwitterError("Sorry, that page does not exist", 34)))
      result should throwAn(expectedTwitterException).await
    }

    "throw twitter exception to generic failure http response" in new OAuthClientSpecContext {
      val body = "Something bad happened"
      val response = HttpResponse(StatusCodes.RequestTimeout, entity = body)
      val result = when(exampleRequest).expectRequest(identity(_)).respondWith(response)
      val expectedTwitterException = TwitterException(code = StatusCodes.RequestTimeout, errors = Errors(body))

      result should throwAn(expectedTwitterException).await
    }

  }

}
