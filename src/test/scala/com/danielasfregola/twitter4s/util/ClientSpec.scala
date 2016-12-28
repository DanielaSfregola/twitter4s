package com.danielasfregola.twitter4s.util

import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.testkit.TestProbe
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import org.specs2.matcher.{NoConcurrentExecutionContext, ThrownExpectations}
import org.specs2.mutable.SpecificationLike
import org.specs2.specification.Scope

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ClientSpec extends ThrownExpectations with SpecificationLike {

  val `application/x-www-form-urlencoded` = MediaTypes.`application/x-www-form-urlencoded` withCharset HttpCharsets.`UTF-8`

  abstract class ClientSpecContext extends TestActorSystem with OAuthClient with FixturesSupport with AwaitableFuture with NoConcurrentExecutionContext with Scope {

    val consumerToken = ConsumerToken("consumer-key", "consumer-secret")
    val accessToken = AccessToken("access-key", "access-secret")

    private val transport = TestProbe()


    override def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T]): Future[T] = {
      implicit val timeout: Timeout = DurationInt(20) seconds
      val requestStartTime =  System.currentTimeMillis
      val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
      for {
        response <- responseR
        t <- unmarshal[T](requestStartTime, f)(request, response)
      } yield t
    }

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
        respondWith(HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, loadJson(resourcePath))))

      def respondWithOk: Future[Unit] = {
        val response = HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, """{"code": "OK"}"""))
        transport.reply(response)
        Future(())
      }
    }

    implicit class RichUri(val uri: Uri) {

      def endpoint = s"${uri.scheme}:${uri.authority}${uri.path}"
    }

  }

}
