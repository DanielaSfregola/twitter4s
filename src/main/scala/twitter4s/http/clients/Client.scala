package twitter4s.http.clients

import scala.concurrent.Future

import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, UnmarshallerLifting}
import twitter4s.http.unmarshalling.JsonSupport
import twitter4s.providers.ActorRefFactoryProvider
import twitter4s.util.ActorContextExtractor

trait Client extends JsonSupport with ActorContextExtractor with UnmarshallerLifting {
  self: ActorRefFactoryProvider =>

  implicit class RichHttpRequest(val request: HttpRequest) {
    def respondAs[T: Manifest]: Future[T] = sendReceiveAs[T](request)
  }

  def sendReceiveAs[T: FromResponseUnmarshaller](request: HttpRequest) =
    pipeline apply request

  // TODO - logRequest, logResponse customisable?
  // TODO - link request and response?
  def pipeline[T: FromResponseUnmarshaller]: HttpRequest => Future[T]

  def sendReceive = spray.client.pipelining.sendReceive

  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info(s"${request.method} ${request.uri}")
    request
  }

  // TODO - improve response logging
  def logResponse: Future[HttpResponse] => Future[HttpResponse] = { futureResponse =>
    futureResponse.map { response =>
      response.status.isSuccess match {
        case true => log.info(response.status.toString)
        case false => log.error(response.toString)
      }
    }
    futureResponse
  }

}
