package com.danielasfregola.twitter4s.http.serializers
import com.danielasfregola.twitter4s.entities.enums._
import com.danielasfregola.twitter4s.entities.v2.{enums => v2}
import org.json4s.Formats
import org.json4s.ext.EnumNameSerializer

private[twitter4s] object EnumFormats extends FormatsComposer {

  override def compose(f: Formats): Formats =
    f +
      new EnumNameSerializer(Alignment) +
      new EnumNameSerializer(ContributorType) +
      new EnumNameSerializer(DisconnectionCode) +
      new EnumNameSerializer(SimpleEventCode) +
      new EnumNameSerializer(TweetEventCode) +
      new EnumNameSerializer(TwitterListEventCode) +
      new EnumNameSerializer(Granularity) +
      new EnumNameSerializer(Hour) +
      new EnumNameSerializer(Language) +
      new EnumNameSerializer(Measure) +
      new EnumNameSerializer(Mode) +
      new EnumNameSerializer(Resource) +
      new EnumNameSerializer(ResultType) +
      new EnumNameSerializer(TimeZone) +
      new EnumNameSerializer(WidgetType) +
      new EnumNameSerializer(WithFilter) +
      new EnumNameSerializer(v2.CoordinatesType) +
      new EnumNameSerializer(v2.ReferencedTweetType) +
      new EnumNameSerializer(v2.TweetReplySetting) +
      new EnumNameSerializer(v2.WithheldScope) +
      new EnumNameSerializer(v2.MediaType)
}
