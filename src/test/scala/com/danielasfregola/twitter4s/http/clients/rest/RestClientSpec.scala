package com.danielasfregola.twitter4s.http.clients.rest

import akka.http.scaladsl.model.{ContentTypes, HttpResponse, StatusCodes}
import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterError, TwitterException}
import com.danielasfregola.twitter4s.http.clients.RestClient
import com.danielasfregola.twitter4s.util.rest.ClientSpec

import scala.concurrent.Future

class RestClientSpec extends ClientSpec {

  class RestClientSpecContext extends ClientSpecContext with RestClient {

    def exampleRequest(): Future[Unit] = Get("an-example-request", ContentTypes.`application/json`).respondAs[Unit]

  }

  "Rest Client" should {

    "throw twitter exception to twitter rejection" in new RestClientSpecContext {
      val response = {
        val entity = """{"errors":[{"message":"Sorry, that page does not exist","code":34}]}"""
        HttpResponse(StatusCodes.NotFound, entity = entity)
      }
      val result = when(exampleRequest).expectRequest(identity(_)).respondWith(response)
      val expectedTwitterException = TwitterException(code = StatusCodes.NotFound,
                                       errors = Errors(TwitterError("Sorry, that page does not exist", 34)))
      result.await should throwAn(expectedTwitterException)
    }

    "throw twitter exception to generic failure http response" in new RestClientSpecContext {
      val body = "Something bad happened"
      val response = HttpResponse(StatusCodes.RequestTimeout, entity = body)
      val result = when(exampleRequest).expectRequest(identity(_)).respondWith(response)
      val expectedTwitterException = TwitterException(code = StatusCodes.RequestTimeout, errors = Errors(body))

      result.await should throwAn(expectedTwitterException)
    }

  }

}
