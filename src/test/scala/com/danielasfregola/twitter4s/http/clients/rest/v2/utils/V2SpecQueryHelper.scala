package com.danielasfregola.twitter4s.http.clients.rest.v2.utils

import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions
import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.{Expansions => UserExpansions}
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.{TweetFields, UserFields}
import java.net.URLEncoder

private[v2] object V2SpecQueryHelper {
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

  def buildIdsParam(ids: Seq[String]): String = "ids=" + encodeQueryParamValue(ids.mkString(","))
  def buildUsernamesParam(usernames: Seq[String]): String = "usernames=" + encodeQueryParamKey(usernames.mkString(","))

  def buildUserExpansions(expansions: Seq[UserExpansions]): String = "expansions=" + encodeQueryParamValue(expansions.mkString(","))

  def buildTweetFieldsParam(fields: Seq[TweetFields]): String = encodeQueryParamKey("tweet.fields") + "=" + encodeQueryParamValue(fields.mkString(","))

  def buildUserFieldsParam(fields: Seq[UserFields]): String = encodeQueryParamKey("user.fields") + "=" + encodeQueryParamValue(fields.mkString(","))

  private def encodeQueryParamKey(str: String) = URLEncoder.encode(str, "UTF-8").replace(".","%2E")
  private def encodeQueryParamValue(str: String) = URLEncoder.encode(str, "UTF-8")
}
