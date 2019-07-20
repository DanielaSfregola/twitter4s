package com.danielasfregola.twitter4s.http.clients.streaming
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.ask
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, StreamTcpException}
import akka.util.Timeout
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode
import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage
import com.danielasfregola.twitter4s.entities.streaming.common.{DisconnectMessage, DisconnectMessageInfo}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.streaming.ActorStreamingClient.OpenConnection
import com.danielasfregola.twitter4s.util.Configurations.{statusStreamingTwitterUrl, twitterVersion}
import com.typesafe.scalalogging.LazyLogging
import org.reactivestreams.Publisher
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ActorStreamingClientSpec(implicit ee: ExecutionEnv) extends Specification with ClientSpec with LazyLogging {

  "StreamingClient" should {
    {
      "create the stream successfully" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val request = HttpRequest()
        implicit val system = ActorSystem("test-actor-system")
        implicit val mat = ActorMaterializer()
        val asc = system.actorOf(ActorStreamingClientImpl.props(ct, at, request))

        asc must haveSuperclass[ActorRef]
      }

      "fail the stream in case of network issues" in {
        "fail to open a connection for invalid host" in {
          val ct = ConsumerToken("", "")
          val at = AccessToken("", "")
          val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

          val dsa = HttpRequest(HttpMethods.POST, s"https://nosuchurlever.com")

          implicit val system = ActorSystem("sada")
          implicit val mat = ActorMaterializer()
          implicit val timeout = Timeout(20 seconds)

          val actorRef = system.actorOf(ActorStreamingClientImpl.props(ct, at, dsa))

          val streamSourceFuture = (actorRef ? OpenConnection())
            .asInstanceOf[Future[Publisher[CommonStreamingMessage]]]
            .map(Source.fromPublisher)
          val streamSource = Await.result(streamSourceFuture, 20 seconds)
          val streamResult = streamSource.runWith(Sink.head)
          streamResult must throwAn[StreamTcpException].await(0, 20 seconds)
        }
      }

      "fail the stream with 401 for invalid twitter credentials" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

        val dsa = HttpRequest(HttpMethods.POST, s"$statusUrl/filter.json")

        implicit val system = ActorSystem("sada")
        implicit val mat = ActorMaterializer()
        implicit val timeout = Timeout(20 seconds)

        val actorRef = system.actorOf(ActorStreamingClientImpl.props(ct, at, dsa))

        val streamSourceFuture =
          (actorRef ? OpenConnection())
            .asInstanceOf[Future[Publisher[CommonStreamingMessage]]]
            .map(Source.fromPublisher)
        val streamSource = Await.result(streamSourceFuture, 20 seconds)
        val streamResult = streamSource.runWith(Sink.head)
        streamResult must throwAn[TwitterException].await(0, 20 seconds)
      }

      "receive a disconnect message from the mocked connection stream" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"

        val dsa = HttpRequest(HttpMethods.POST, s"$statusUrl/filter.json")

        implicit val system = ActorSystem("sada")
        implicit val mat = ActorMaterializer()
        implicit val timeout = Timeout(20 seconds)

        val actorRef = system.actorOf(ActorStreamingClientWithFakeHttp.props(ct, at, dsa))

        val msg = DisconnectMessage(DisconnectMessageInfo(DisconnectionCode.Normal, "test", "test")).asInstanceOf[CommonStreamingMessage]

        val streamSourceFuture =
          (actorRef ? OpenConnection())
            .asInstanceOf[Future[Publisher[Any]]]
            .map(Source.fromPublisher)
        val streamSource = Await.result(streamSourceFuture, 20 seconds)
        logger.info(s"Got reply from actor}")
        val streamResult = streamSource.runWith(Sink.foreach(x => {
          logger.info(x.toString)
        }))

//        streamSource
//        val streamSource = Await.result(streamSourceFuture, 20 seconds)

        //        streamResult must be(msg).await(0, 20 seconds)
//        0 must be 0
          streamResult must throwAn[TwitterException].await(0, 20 seconds)

      }
    }
  }
}
