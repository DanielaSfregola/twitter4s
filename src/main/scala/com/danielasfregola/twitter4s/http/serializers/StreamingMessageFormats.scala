package com.danielasfregola.twitter4s.http.serializers

import com.danielasfregola.twitter4s.entities.streaming.common._
import com.danielasfregola.twitter4s.entities.streaming.site._
import com.danielasfregola.twitter4s.entities.streaming.user._
import com.danielasfregola.twitter4s.entities.streaming.{
  CommonStreamingMessage,
  SiteStreamingMessage,
  StreamingMessage,
  UserStreamingMessage
}
import com.danielasfregola.twitter4s.entities.{DirectMessage, Tweet}
import org.json4s.JsonAST.JValue
import org.json4s.{CustomSerializer, Extraction, FieldSerializer, Formats, MappingException}

private[twitter4s] object StreamingMessageFormats extends FormatsComposer {

  override def compose(f: Formats): Formats =
    f + StreamingMessageSerializer + tweetUnmarshaller

  private val tweetUnmarshaller = FieldSerializer[Tweet](deserializer = FieldSerializer.renameFrom("full_text", "text"))

  private def withCustomUnmarshaller[T <: StreamingMessage: Manifest](json: JValue, formatter: Formats): Option[T] = {
    implicit val _: Formats = formatter
    Extraction.extractOpt[T](json)
  }

  def findOrExplode[T <: StreamingMessage: Manifest](json: JValue)(ops: Stream[() => Option[T]])(
      implicit formats: Formats): T = {
    val maybeT = ops.map(f => f()).filter(opt => opt.isDefined).head
    maybeT match {
      case Some(t) => t
      case None =>
        val className = manifest[T].getClass.getSimpleName
        throw new MappingException(s"Could not recognise json as $className: ${json.extract[String]}")
    }
  }

  object StreamingMessageSerializer
      extends CustomSerializer[StreamingMessage](implicit format =>
        ({
          case json => findOrExplode(json)(streamingMessageStream(json)(format + tweetUnmarshaller))
        }, {
          case disconnectMsg: DisconnectMessage                   => Extraction.decompose(disconnectMsg)
          case limitNotice: LimitNotice                           => Extraction.decompose(limitNotice)
          case locationDeletionNotice: LocationDeletionNotice     => Extraction.decompose(locationDeletionNotice)
          case statusDeletionNotice: StatusDeletionNotice         => Extraction.decompose(statusDeletionNotice)
          case statusWithheldNotice: StatusWithheldNotice         => Extraction.decompose(statusWithheldNotice)
          case userWithheldNotice: UserWithheldNotice             => Extraction.decompose(userWithheldNotice)
          case warningMessage: WarningMessage                     => Extraction.decompose(warningMessage)
          case twitterListEvent: TwitterListEvent                 => Extraction.decompose(twitterListEvent)
          case tweetEvent: TweetEvent                             => Extraction.decompose(tweetEvent)
          case simpleEvent: SimpleEvent                           => Extraction.decompose(simpleEvent)
          case controlMessage: ControlMessage                     => Extraction.decompose(controlMessage)
          case userEnvelopTweet: UserEnvelopTweet                 => Extraction.decompose(userEnvelopTweet)
          case userEnvelopDirectMessage: UserEnvelopDirectMessage => Extraction.decompose(userEnvelopDirectMessage)
          case userEnvelopTwitterListEvent: UserEnvelopTwitterListEvent =>
            Extraction.decompose(userEnvelopTwitterListEvent)
          case userEnvelopTweetEvent: UserEnvelopTweetEvent         => Extraction.decompose(userEnvelopTweetEvent)
          case userEnvelopSimpleEvent: UserEnvelopSimpleEvent       => Extraction.decompose(userEnvelopSimpleEvent)
          case userEnvelopWarningMessage: UserEnvelopWarningMessage => Extraction.decompose(userEnvelopWarningMessage)
          case userEnvelopTweetStringified: UserEnvelopTweetStringified =>
            Extraction.decompose(userEnvelopTweetStringified)
          case userEnvelopDirectMessageStringified: UserEnvelopDirectMessageStringified =>
            Extraction.decompose(userEnvelopDirectMessageStringified)
          case userEnvelopTwitterListEventStringified: UserEnvelopTwitterListEventStringified =>
            Extraction.decompose(userEnvelopTwitterListEventStringified)
          case userEnvelopTweetEventStringified: UserEnvelopTweetEventStringified =>
            Extraction.decompose(userEnvelopTweetEventStringified)
          case userEnvelopSimpleEventStringified: UserEnvelopSimpleEventStringified =>
            Extraction.decompose(userEnvelopSimpleEventStringified)
          case userEnvelopWarningMessageStringified: UserEnvelopWarningMessageStringified =>
            Extraction.decompose(userEnvelopWarningMessageStringified)
          case userEnvelopFriendsLists: UserEnvelopFriendsLists => Extraction.decompose(userEnvelopFriendsLists)
          case userEnvelopFriendsListsStringified: UserEnvelopFriendsListsStringified =>
            Extraction.decompose(userEnvelopFriendsListsStringified)
          case friendsLists: FriendsLists                       => Extraction.decompose(friendsLists)
          case friendsListsStringified: FriendsListsStringified => Extraction.decompose(friendsListsStringified)
        }))

  def streamingMessageStream(json: JValue)(implicit formats: Formats): Stream[() => Option[StreamingMessage]] = {
    commonStreamingMessageStream(json) ++
      userStreamingMessageStream(json) ++
      siteStreamingMessageStream(json) ++
      Stream(
        () => Extraction.extractOpt[UserEnvelopFriendsLists](json),
        () => Extraction.extractOpt[UserEnvelopFriendsListsStringified](json),
        () => Extraction.extractOpt[FriendsLists](json),
        () => Extraction.extractOpt[FriendsListsStringified](json)
      )
  }

  private def commonStreamingMessageStream(json: JValue)(
      implicit formats: Formats): Stream[() => Option[CommonStreamingMessage]] =
    Stream(
      () => withCustomUnmarshaller[Tweet](json, formats + tweetUnmarshaller),
      () => Extraction.extractOpt[DisconnectMessage](json),
      () => Extraction.extractOpt[LimitNotice](json),
      () => Extraction.extractOpt[LocationDeletionNotice](json),
      () => Extraction.extractOpt[StatusDeletionNotice](json),
      () => Extraction.extractOpt[StatusWithheldNotice](json),
      () => Extraction.extractOpt[UserWithheldNotice](json),
      () => Extraction.extractOpt[WarningMessage](json)
    )

  private def userStreamingMessageStream(json: JValue)(
      implicit formats: Formats): Stream[() => Option[UserStreamingMessage]] =
    Stream(
      () => Extraction.extractOpt[DirectMessage](json),
      () => Extraction.extractOpt[TwitterListEvent](json),
      () => Extraction.extractOpt[TweetEvent](json),
      () => Extraction.extractOpt[SimpleEvent](json)
    )

  private def siteStreamingMessageStream(json: JValue)(
      implicit formats: Formats): Stream[() => Option[SiteStreamingMessage]] =
    Stream(
      () => Extraction.extractOpt[ControlMessage](json),
      () => Extraction.extractOpt[UserEnvelopTweet](json),
      () => Extraction.extractOpt[UserEnvelopDirectMessage](json),
      () => Extraction.extractOpt[UserEnvelopTwitterListEvent](json),
      () => Extraction.extractOpt[UserEnvelopTweetEvent](json),
      () => Extraction.extractOpt[UserEnvelopSimpleEvent](json),
      () => Extraction.extractOpt[UserEnvelopWarningMessage](json),
      () => Extraction.extractOpt[UserEnvelopTweetStringified](json),
      () => Extraction.extractOpt[UserEnvelopDirectMessageStringified](json),
      () => Extraction.extractOpt[UserEnvelopTwitterListEventStringified](json),
      () => Extraction.extractOpt[UserEnvelopTweetEventStringified](json),
      () => Extraction.extractOpt[UserEnvelopSimpleEventStringified](json),
      () => Extraction.extractOpt[UserEnvelopWarningMessageStringified](json)
    )
}
