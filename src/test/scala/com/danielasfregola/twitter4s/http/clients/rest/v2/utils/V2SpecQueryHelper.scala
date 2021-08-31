package com.danielasfregola.twitter4s.http.clients.rest.v2.utils

import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.TweetExpansions.TweetExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.UserExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.MediaFields.MediaFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.{MediaFields, TweetFields, UserFields}
import java.net.URLEncoder

private[v2] object V2SpecQueryHelper {
  val allTweetExpansions: Seq[TweetExpansions] = Seq(
    TweetExpansions.AuthorId,
    TweetExpansions.`Entities.Mentions.Username`,
    TweetExpansions.InReplyToUser,
    TweetExpansions.`ReferencedTweets.Id`,
    TweetExpansions.`ReferencedTweets.Id.AuthorId`
  )

  val allUserExpansions: Seq[UserExpansions] = Seq(
    UserExpansions.PinnedTweetId
  )

  val allTweetFields: Seq[TweetFields] = Seq(
    TweetFields.Attachments,
    TweetFields.AuthorId,
    TweetFields.ContextAnnotations,
    TweetFields.ConversationId,
    TweetFields.CreatedAt,
    TweetFields.Entities,
    TweetFields.Geo,
    TweetFields.Id,
    TweetFields.InReplyToUserId,
    TweetFields.Lang,
    TweetFields.NonPublicMetrics,
    TweetFields.PublicMetrics,
    TweetFields.OrganicMetrics,
    TweetFields.PromotedMetrics,
    TweetFields.PossiblySensitive,
    TweetFields.ReferencedTweets,
    TweetFields.ReplySettings,
    TweetFields.Source,
    TweetFields.Text,
    TweetFields.Withheld
  )

  val allUserFields: Seq[UserFields] = Seq(
    UserFields.CreatedAt,
    UserFields.Description,
    UserFields.Entities,
    UserFields.Id,
    UserFields.Location,
    UserFields.Name,
    UserFields.PinnedTweetId,
    UserFields.ProfileImageUrl,
    UserFields.Protected,
    UserFields.PublicMetrics,
    UserFields.Url,
    UserFields.Username,
    UserFields.Verified,
    UserFields.Withheld
  )

  val allMediaFields: Seq[MediaFields] = Seq(
    MediaFields.DurationMs,
    MediaFields.Height,
    MediaFields.MediaKey,
    MediaFields.PreviewImageUrl,
    MediaFields.Type,
    MediaFields.Url,
    MediaFields.Width,
    MediaFields.PublicMetrics,
    MediaFields.NonPublicMetrics,
    MediaFields.OrganicMetrics,
    MediaFields.PromotedMetrics
  )

  def buildIdsParam(ids: Seq[String]): String = "ids=" + encodeQueryParamValue(ids.mkString(","))
  def buildUsernamesParam(usernames: Seq[String]): String = "usernames=" + encodeQueryParamKey(usernames.mkString(","))

  def buildTweetExpansions(expansions: Seq[TweetExpansions]): String =
    "expansions=" + encodeQueryParamValue(expansions.mkString(","))
  def buildUserExpansions(expansions: Seq[UserExpansions]): String =
    "expansions=" + encodeQueryParamValue(expansions.mkString(","))

  def buildTweetFieldsParam(fields: Seq[TweetFields]): String =
    encodeQueryParamKey("tweet.fields") + "=" + encodeQueryParamValue(fields.mkString(","))

  def buildUserFieldsParam(fields: Seq[UserFields]): String =
    encodeQueryParamKey("user.fields") + "=" + encodeQueryParamValue(fields.mkString(","))

  def buildMediaFieldsParam(fields: Seq[MediaFields]): String =
    encodeQueryParamKey("media.fields") + "=" + encodeQueryParamValue(fields.mkString(","))

  private def encodeQueryParamKey(str: String) = URLEncoder.encode(str, "UTF-8").replace(".", "%2E")
  private def encodeQueryParamValue(str: String) = URLEncoder.encode(str, "UTF-8")
}
