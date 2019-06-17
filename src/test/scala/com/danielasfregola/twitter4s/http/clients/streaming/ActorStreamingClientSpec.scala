package com.danielasfregola.twitter4s.http.clients.streaming
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.helpers.{ClientSpec, TestActorSystem, TestExecutionContext}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.streaming.ActorStreamingClient.{GetPublisher, OpenConnection}
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import com.danielasfregola.twitter4s.util.Configurations.{statusStreamingTwitterUrl, twitterVersion}
import com.typesafe.scalalogging.LazyLogging
import org.reactivestreams.{Publisher, Subscriber, Subscription}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

class ActorStreamingClientSpec(implicit ee: ExecutionEnv)
    extends Specification
    with ClientSpec
    with OAuthClient
    with LazyLogging  {

  "StreamingClient" should {
    {
      "be created successfully" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val request = HttpRequest()
        implicit val system = ActorSystem("sada")
        implicit val mat = ActorMaterializer()
        val asc = system.actorOf(ActorStreamingClient.props(ct, at, request))

        asc must not be null
      }

      "fail to open a connection for bad credentials" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"
        val request = Post(s"$statusUrl/filter.json", "")

        val dsa = HttpRequest(HttpMethods.POST, s"$statusUrl/filter.json")

        implicit val system = ActorSystem("sada")
        implicit val mat = ActorMaterializer()

        val actorRef = system.actorOf(ActorStreamingClient.props(ct, at, request))

//        val actorRef = TestActorRef(new ActorStreamingClient(ct, at, dsa))

        implicit val timeout = Timeout(20 seconds)

        val res = actorRef ? GetPublisher()

        val publisher = Await.result(res, 20 seconds).asInstanceOf[Publisher[CommonStreamingMessage]]

        val dd2 = Source.fromPublisher(publisher).runWith(Sink.head)
        val result = Await.result(actorRef ? OpenConnection(), 5000 millis)
        dd2 must throwAn[Exception].await
      }
    }
  }
  override def oauthProvider: OAuth1Provider = new OAuth1Provider(ConsumerToken("", ""), Some(AccessToken("", "")))
  override def withLogRequest: Boolean = true
  override def withLogRequestResponse: Boolean = true
}
