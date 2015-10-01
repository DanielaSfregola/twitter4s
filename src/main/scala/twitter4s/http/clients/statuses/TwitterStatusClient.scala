package twitter4s.http
package clients.statuses

import scala.concurrent.Future

import twitter4s.entities.{OEmbedTweet, Tweet}
import twitter4s.entities.enums.Alignment.Alignment
import twitter4s.entities.enums.Language.Language
import twitter4s.entities.enums.WidgetType.WidgetType
import twitter4s.entities.enums.{Alignment, Language}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.statuses.entities.StatusUpdate
import twitter4s.http.clients.statuses.parameters._
import twitter4s.util.Configurations

trait TwitterStatusClient extends OAuthClient with Configurations {

  val statusesUrl = s"$twitterUrl/$apiVersion/statuses"

  def mentionsTimeline(count: Int = 200,
                       since_id: Option[Long] = None,
                       max_id: Option[Long] = None,
                       trim_user: Boolean = false,
                       contributor_details: Boolean = false,
                       include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = MentionsParameters(count, since_id, max_id, trim_user, contributor_details, include_entities)
    Get(s"$statusesUrl/mentions_timeline.json?$parameters").respondAs[Seq[Tweet]]
  }

  def userTimeline(user_id: Option[Long] = None,
                   screen_name: Option[String] = None,
                   since_id: Option[Long] = None,
                   count: Int = 200,
                   max_id: Option[Long] = None,
                   trim_user: Boolean = false,
                   exclude_replies: Boolean = false,
                   contributor_details: Boolean = false,
                   include_rts: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = UserTimelineParameters(user_id, screen_name, since_id, count, max_id, trim_user, exclude_replies, contributor_details, include_rts)
    Get(s"$statusesUrl/user_timeline.json?$parameters").respondAs[Seq[Tweet]]
  }

  def homeTimeline(count: Int = 200,
                   since_id: Option[Long] = None,
                   max_id: Option[Long] = None,
                   trim_user: Boolean = false,
                   exclude_replies: Boolean = false,
                   contributor_details: Boolean = false,
                   include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = HomeTimelineParameters(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)
    Get(s"$statusesUrl/home_timeline.json?$parameters").respondAs[Seq[Tweet]]
  }

  def retweetsOfMe(count: Int = 200,
                   since_id: Option[Long] = None,
                   max_id: Option[Long] = None,
                   trim_user: Boolean = false,
                   exclude_replies: Boolean = false,
                   contributor_details: Boolean = false,
                   include_entities: Boolean = true): Future[Seq[Tweet]] = {
    val parameters = RetweetsOfMeParameters(count, since_id, max_id, trim_user, exclude_replies, contributor_details, include_entities)
    Get(s"$statusesUrl/retweets_of_me.json?$parameters").respondAs[Seq[Tweet]]
  }

  def retweets(id: Long,
               count: Int = 100,
               trim_user: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = RetweetsParameters(count, trim_user)
    Get(s"$statusesUrl/retweets/$id.json?$parameters").respondAs[Seq[Tweet]]
  }

  def show(id: Long,
          trim_user: Boolean = false,
          include_my_retweet: Boolean = false,
          include_entities: Boolean = true): Future[Tweet] = {
    val parameters = ShowParameters(id, trim_user, include_my_retweet, include_entities)
    Get(s"$statusesUrl/show.json?$parameters").respondAs[Tweet]
  }

  def destroy(id: Long,
              trim_user: Boolean = false): Future[Tweet] = {
    val parameters = PostParameters(trim_user)
    Post(s"$statusesUrl/destroy/$id.json?$parameters").respondAs[Tweet]
  }

  def update(status: String,
             in_reply_to_status_id: Option[Long] = None,
             possibly_sensitive: Boolean = false,
             lat: Option[Long] = None,
             long: Option[Long] = None,
             place_id: Option[String] = None,
             display_coordinates: Boolean = false,
             trim_user: Boolean = false,
             media_ids: Seq[Long] = Seq.empty): Future[Tweet] = {
    val entity = StatusUpdate(status, in_reply_to_status_id, possibly_sensitive, lat, long, place_id, display_coordinates, trim_user, media_ids)
    Post(s"$statusesUrl/update.json", entity).respondAs[Tweet]
  }

  def directMessage(message: String,
                    username: String,
                    in_reply_to_status_id: Option[Long] = None,
                    possibly_sensitive: Boolean = false,
                    lat: Option[Long] = None,
                    long: Option[Long] = None,
                    place_id: Option[String] = None,
                    display_coordinates: Boolean = false,
                    trim_user: Boolean = false,
                    media_ids: Seq[Long] = Seq.empty): Future[Tweet] = {
    val directMessage = s"D $username $message"
    update(directMessage, in_reply_to_status_id, possibly_sensitive, lat, long, place_id, display_coordinates, trim_user, media_ids)
  }
  
  def retweet(id: Long, trim_user: Boolean = false): Future[Tweet] = {
    val parameters = PostParameters(trim_user)
    Post(s"$statusesUrl/retweet/$id.json?$parameters").respondAs[Tweet]
  }

  def oembedById(id: Long,
             maxwidth: Option[Int] = None,
             hide_media: Boolean = false,
             hide_thread: Boolean = false,
             omit_script: Boolean = false,
             align: Alignment = Alignment.None,
             related: Seq[String] = Seq.empty,
             lang: Language = Language.English,
             widget_type: Option[WidgetType] = None,
             hide_tweet: Boolean = false): Future[OEmbedTweet] = {
    val parameters = OEmbedParametersById(id, maxwidth, hide_media, hide_thread, omit_script, align, related, lang, widget_type, hide_tweet)
    Get(s"$statusesUrl/oembed.json?$parameters").respondAs[OEmbedTweet]
  }

  def oembedByUrl(url: String,
             maxwidth: Option[Int] = None,
             hide_media: Boolean = false,
             hide_thread: Boolean = false,
             omit_script: Boolean = false,
             align: Alignment = Alignment.None,
             related: Seq[String] = Seq.empty,
             lang: Language = Language.English,
             widget_type: Option[WidgetType] = None,
             hide_tweet: Boolean = false): Future[OEmbedTweet] = {
    val parameters = OEmbedParametersByUrl(url.urlEncoded, maxwidth, hide_media, hide_thread, omit_script, align, related, lang, widget_type, hide_tweet)
    Get(s"$statusesUrl/oembed.json?$parameters").respondAs[OEmbedTweet]
  }

}

