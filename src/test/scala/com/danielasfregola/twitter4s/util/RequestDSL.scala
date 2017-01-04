package com.danielasfregola.twitter4s.util

import akka.http.scaladsl.model._
import akka.testkit.TestProbe

import scala.concurrent.Future

abstract class RequestDSL extends TestActorSystem with FixturesSupport {

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
    def respondWith(res: HttpResponse): Future[T] = {
      transport.reply(res); future
    }

    def respondWith(resourcePath: String): Future[T] =
      respondWith(HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, load(resourcePath))))

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
