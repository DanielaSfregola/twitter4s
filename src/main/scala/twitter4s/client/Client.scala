package twitter4s.client

import scala.concurrent.Future

import spray.client.pipelining._
import spray.http.{HttpResponse, HttpRequest}
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import twitter4s.json.JsonSupport
import twitter4s.providers.{ActorRefFactoryProvider}
import twitter4s.utils.ActorContextExtractor

trait Client extends JsonSupport with ActorContextExtractor {
  self: ActorRefFactoryProvider =>

  def sendReceiveAs[T: FromResponseUnmarshaller](request: HttpRequest) = pipeline[T] apply request

  implicit def toResponse[T: FromResponseUnmarshaller](request: HttpRequest): Future[T] = sendReceiveAs[T](request)

  // TODO - logRequest, logResponse customisable?
  // TODO - link request and response?
  def pipeline[T: FromResponseUnmarshaller] =
    logRequest ~> sendReceive ~> logResponse ~> unmarshal[T]

  def sendReceive = spray.client.pipelining.sendReceive

  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info(s"${request.method.toString} ${request.uri.toString}")
    request
  }

  def logResponse: Future[HttpResponse] => Future[HttpResponse] = { response =>
    response.map { res =>
      log.info(res.toString)
    }
    response
  }

}
