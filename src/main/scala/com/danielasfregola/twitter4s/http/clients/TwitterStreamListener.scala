package com.danielasfregola.twitter4s.http.clients

import akka.actor.{Actor, ActorRef, Props}
import com.danielasfregola.twitter4s.entities.streaming._

private[twitter4s] abstract class TwitterStreamListener extends Actor {

  def receive: Receive = {
    case StreamingUpdate(streamingEvent: CommonStreamingMessage) => processCommonStreamingMsg(streamingEvent)
    case StreamingUpdate(streamingEvent: SiteStreamingMessage) => processSiteStreamingMsg(streamingEvent)
    case StreamingUpdate(streamingEvent: UserStreamingMessage) => processUserStreamingMsg(streamingEvent)
  }

  def processSiteStreamingMsg: SiteStreamingMessage => Unit
  def processUserStreamingMsg: UserStreamingMessage => Unit
  def processCommonStreamingMsg: CommonStreamingMessage => Unit

}

private[twitter4s] trait TwitterStreamListenerHelper {
  self: StreamingOAuthClient =>

  private def ignore: PartialFunction[StreamingMessage, Unit] = { case _ => }

  def createCommonListener(f: PartialFunction[CommonStreamingMessage, Unit]): ActorRef =
    system.actorOf(Props(new TwitterStreamListener {
      def processCommonStreamingMsg: CommonStreamingMessage => Unit = f orElse ignore
      def processSiteStreamingMsg: SiteStreamingMessage => Unit = ignore
      def processUserStreamingMsg: UserStreamingMessage => Unit = ignore
    }))

  def createUserListener(f: PartialFunction[UserStreamingMessage, Unit]): ActorRef =
    system.actorOf(Props(new TwitterStreamListener {
      def processCommonStreamingMsg: CommonStreamingMessage => Unit = processUserStreamingMsg
      def processSiteStreamingMsg: SiteStreamingMessage => Unit = ignore
      def processUserStreamingMsg: UserStreamingMessage => Unit = f orElse ignore
    }))

  def createSiteListener(f: PartialFunction[SiteStreamingMessage, Unit]): ActorRef =
    system.actorOf(Props(new TwitterStreamListener {
      def processCommonStreamingMsg: CommonStreamingMessage => Unit = processSiteStreamingMsg
      def processSiteStreamingMsg: SiteStreamingMessage => Unit = f orElse ignore
      def processUserStreamingMsg: UserStreamingMessage => Unit = ignore
    }))

}
