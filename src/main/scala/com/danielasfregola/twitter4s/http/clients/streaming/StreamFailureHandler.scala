package com.danielasfregola.twitter4s.http.clients.streaming
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode._
import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage
import com.danielasfregola.twitter4s.entities.streaming.common.{DisconnectMessage, LimitNotice}

trait StreamFailureHandler {

  def handleMessage(csm: CommonStreamingMessage): StreamFailureAction.Value = {
    csm match{
      case dm: DisconnectMessage =>
        dm.disconnect.code match {
          case Shutdown        => StreamFailureAction.RetryImmediately
          case DuplicateStream => StreamFailureAction.RetryImmediately
          case ControlRequest  => StreamFailureAction.RetryImmediately
          case Stall           => StreamFailureAction.RetryImmediately
          case Normal          => StreamFailureAction.RetryImmediately
          case TokenRevoked    => StreamFailureAction.RetryImmediately
          case AdminLogout     => StreamFailureAction.RetryImmediately
          case Internal        => StreamFailureAction.RetryImmediately
          case MaxMessageLimit => StreamFailureAction.RetryImmediately
          case StreamException => StreamFailureAction.RetryImmediately
          case BrokerStall     => StreamFailureAction.RetryImmediately
          case ShedLoad        => StreamFailureAction.RetryImmediately
          case _               => StreamFailureAction.RetryImmediately
        }
      case lm: LimitNotice => StreamFailureAction.RetryWithBigBackoff
      case _               => StreamFailureAction.NoRetry
    }
  }
}

object StreamFailureAction extends Enumeration {

  type StreamFailureActionDelay = Value
  // miliseconds till first reconnect

  val NoRetry = Value(0, "none")
  val RetryImmediately = Value(0, "no-backoff")
  val RetryWithBigBackoff = Value(60000, "exponential")
  val RetryForHttpError = Value(5000, "linear")
  val RetryForTcpIpError = Value(250, "linear")
}
