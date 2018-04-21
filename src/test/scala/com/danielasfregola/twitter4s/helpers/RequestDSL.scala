package com.danielasfregola.twitter4s.helpers

import java.text.SimpleDateFormat
import java.util.Locale

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.testkit.TestProbe
import com.danielasfregola.twitter4s.entities.RateLimit
import org.specs2.specification.AfterEach

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

abstract class RequestDSL extends TestActorSystem with FixturesSupport with AfterEach {

  def after = system.terminate

  private val timeout = 10 seconds

  val headers = List(RawHeader("x-rate-limit-limit", "15"),
                     RawHeader("x-rate-limit-remaining", "14"),
                     RawHeader("x-rate-limit-reset", "1445181993"))

  val rateLimit = {
    val dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
    val resetDate = dateFormatter.parse("Sun Oct 18 15:26:33 +0000 2015").toInstant
    new RateLimit(limit = 15, remaining = 14, reset = resetDate)
  }

  protected val transport = TestProbe()

  def when[T](future: Future[T]): RequestMatcher[T] = new RequestMatcher(future)

  class RequestMatcher[T](future: Future[T]) {
    protected def responder = new Responder(future)

    def expectRequest(req: HttpRequest): Responder[T] = {
      transport.expectMsg(timeout, req)
      responder
    }

    def expectRequest(fn: HttpRequest => Unit) = {
      transport.expectMsgPF(timeout) {
        case req: HttpRequest => fn(req)
      }
      responder
    }
  }

  class Responder[T](future: Future[T]) {
    def respondWith(response: HttpResponse): Await[T] = {
      transport.reply(response)
      new Await(future)
    }

    def respondWith(resourcePath: String): Await[T] =
      respondWith(HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, load(resourcePath))))

    def respondWithRated(resourcePath: String): Await[T] =
      respondWith(
        HttpResponse(StatusCodes.OK,
                     headers = headers,
                     entity = HttpEntity(MediaTypes.`application/json`, load(resourcePath))))

    def respondWithOk: Await[Unit] = {
      val response =
        HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, """{"code": "OK"}"""))
      transport.reply(response)
      new Await(Future.successful((): Unit))
    }
  }

  class Await[T](future: Future[T]) {
    private[helpers] val underlyingFuture = future

    def await(implicit duration: FiniteDuration = 20 seconds) =
      Await.result(future, duration)
  }

  implicit def awaitToReqMatcher[T](await: Await[T]) =
    new RequestMatcher(await.underlyingFuture)

}
