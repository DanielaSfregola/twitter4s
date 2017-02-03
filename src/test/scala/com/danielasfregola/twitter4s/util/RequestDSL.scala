package com.danielasfregola.twitter4s.util

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.testkit.TestProbe
import com.danielasfregola.twitter4s.entities.{RateLimit, RatedData}

import scala.concurrent.Future

abstract class RequestDSL extends TestActorSystem with FixturesSupport {

  val headers = List(RawHeader("x-rate-limit-limit", "15"),
                     RawHeader("x-rate-limit-remaining", "14"),
                     RawHeader("x-rate-limit-reset", "1445181993"))

  val rateLimit = {
    val dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
    val resetDate = dateFormatter.parse("Sun Oct 18 15:26:33 +0000 2015")
    new RateLimit(limit = 15, remaining = 14, reset = resetDate)
  }

  protected val transport = TestProbe()

  def when[T](future: Future[T]): RequestMatcher[T] = new RequestMatcher(future)

  class RequestMatcher[T](future: Future[T]) {
    protected def responder = new Responder(future)

    def expectRequest(req: HttpRequest): Responder[T] = {
      transport.expectMsg(req)
      responder
    }

    def expectRequest(fn: HttpRequest => Unit) = {
      transport.expectMsgPF() {
        case req: HttpRequest => fn(req)
      }
      responder
    }
  }

  class Responder[T](future: Future[T]) {
    def respondWith(response: HttpResponse): Future[T] = {
      transport.reply(response); future
    }

    def respondWith(resourcePath: String): Future[T] =
      respondWith(HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, load(resourcePath))))

    def respondWithRated(resourcePath: String): Future[T] =
      respondWith(HttpResponse(StatusCodes.OK, headers = headers, entity = HttpEntity(MediaTypes.`application/json`, load(resourcePath))))


    def respondWithOk: Future[Unit] = {
      val response = HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, """{"code": "OK"}"""))
      transport.reply(response)
      Future.successful(())
    }
  }

  implicit class RichUri(val uri: Uri) {

    def endpoint = s"${uri.scheme}:${uri.authority}${uri.path}"
  }

}
