package com.danielasfregola.twitter4s.http.serializers

import com.danielasfregola.twitter4s.entities.enums._
import org.json4s.ext.EnumNameSerializer

object EnumSerializers {

  val all = Seq(new EnumNameSerializer(Alignment),
                new EnumNameSerializer(ContributorType),
                new EnumNameSerializer(DisconnectionCode),
                new EnumNameSerializer(SimpleEventCode),
                new EnumNameSerializer(TweetEventCode),
                new EnumNameSerializer(TwitterListEventCode),
                new EnumNameSerializer(Granularity),
                new EnumNameSerializer(Hour),
                new EnumNameSerializer(Language),
                new EnumNameSerializer(Measure),
                new EnumNameSerializer(Mode),
                new EnumNameSerializer(Resource),
                new EnumNameSerializer(ResultType),
                new EnumNameSerializer(TimeZone),
                new EnumNameSerializer(WidgetType),
                new EnumNameSerializer(WithFilter))
}
