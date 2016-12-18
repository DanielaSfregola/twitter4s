package com.danielasfregola.twitter4s.http.clients.rest.help

import scala.concurrent.Future
import com.danielasfregola.twitter4s.entities.{LanguageDetails, TermsOfService, PrivacyPolicy, Configuration}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `help` resource.
  * */
trait TwitterHelpClient extends OAuthClient with Configurations {

  private val helpUrl = s"$apiTwitterUrl/$twitterVersion/help"

  /** Returns the current configuration used by Twitter including twitter.com slugs which are not usernames, maximum photo resolutions, and t.co shortened URL length.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/help/configuration" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/help/configuration</a>.
    *
    * @return : The current Twitter configuration.
    * */
  def configuration(): Future[Configuration] =
    Get(s"$helpUrl/configuration.json").respondAs[Configuration]

  @deprecated("use configuration instead", "2.2")
  def getConfiguration(): Future[Configuration] =
    configuration()

  /** Returns the list of languages supported by Twitter along with the language code supported by Twitter.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/help/languages" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/help/languages</a>.
    *
    * @return : The list of languages supported by Twitter.
    * */
  def supportedLanguages(): Future[Seq[LanguageDetails]] =
    Get(s"$helpUrl/languages.json").respondAs[Seq[LanguageDetails]]

  @deprecated("use supportedLanguages instead", "2.2")
  def getSupportedLanguages(): Future[Seq[LanguageDetails]] =
    supportedLanguages()

  /** Returns Twitter’s Privacy Policy.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/help/privacy" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/help/privacy</a>.
    *
    * @return : The Twitter's Privacy Policy.
    * */
  def privacyPolicy(): Future[PrivacyPolicy] =
    Get(s"$helpUrl/privacy.json").respondAs[PrivacyPolicy]

  @deprecated("use privacyPolicy instead", "2.2")
  def getPrivacyPolicy(): Future[PrivacyPolicy] =
    privacyPolicy()

  /** Returns the Twitter Terms of Service.
    * Note: these are not the same as the Developer Policy.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/help/tos" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/help/tos</a>.
    *
    * @return : the Twitter Terms of Service.
   * */
  def termsOfService(): Future[TermsOfService] =
    Get(s"$helpUrl/tos.json").respondAs[TermsOfService]

  @deprecated("use termsOfService instead", "2.2")
  def getTermsOfService(): Future[TermsOfService] =
    termsOfService()
}
