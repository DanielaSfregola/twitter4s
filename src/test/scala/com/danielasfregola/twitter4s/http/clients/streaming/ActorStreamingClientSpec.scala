package com.danielasfregola.twitter4s.http.clients.streaming
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.ask
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

import scala.concurrent.Await
import scala.concurrent.duration._

class ActorStreamingClientSpec extends TestActorSystem with ClientSpec with TestExecutionContext with OAuthClient with LazyLogging {

  "StreamingClient" should {
    {
      "be created successfully" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val request = HttpRequest()
        val asc = system.actorOf(ActorStreamingClient.props(ct, at, request))

        asc must not be null
      }

      "fail to open a connection for bad credentials" in {
        val ct = ConsumerToken("", "")
        val at = AccessToken("", "")
        val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"
        val request = Post(s"$statusUrl/filter.json", "")

        val dsa = HttpRequest(HttpMethods.POST, s"$statusUrl/filter.json")

//                val asc = system.actorOf(ActorStreamingClient.props(ct, at, request))

        val actorRef = TestActorRef(new ActorStreamingClient(ct, at, dsa))

        implicit val timeout = Timeout(20 seconds)

        val res = actorRef ? GetPublisher()

        val publisher = Await.result(res, 20 seconds).asInstanceOf[Publisher[CommonStreamingMessage]]

        publisher.subscribe(new Subscriber[CommonStreamingMessage] {
          override def onSubscribe(s: Subscription): Unit = {
            println(s)
          }
          override def onNext(t: CommonStreamingMessage): Unit = {
            println(t)
          }
          override def onError(t: Throwable): Unit = {
            println(t)
          }
          override def onComplete(): Unit = {
            println("Done")
          }
        })
        for(i <- 1 to 10){
          actorRef ! OpenConnection()
          Thread.sleep(1000)

        }
        Thread.sleep(10000)
        publisher must not be null
      }
    }
  }
  override def oauthProvider: OAuth1Provider = new OAuth1Provider(ConsumerToken("", ""), Some(AccessToken("", "")))
  override def withLogRequest: Boolean = true
  override def withLogRequestResponse: Boolean = true
}
