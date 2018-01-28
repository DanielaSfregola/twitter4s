package com.danielasfregola.twitter4s.http.clients.rest.help

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `help` resource.
  * */
trait TwitterHelpClient {

  protected val restClient: RestClient

  private val helpUrl = s"$apiTwitterUrl/$twitterVersion/help"

  /** Returns the current configuration used by Twitter including twitter.com slugs which are not usernames, maximum photo resolutions, and t.co shortened URL length.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/developer-utilities/configuration/api-reference/get-help-configuration" target="_blank">
    *   https://developer.twitter.com/en/docs/developer-utilities/configuration/api-reference/get-help-configuration</a>.
    *
    * @return : The current Twitter configuration.
    * */
  def configuration(): Future[RatedData[Configuration]] = {
    import restClient._
    Get(s"$helpUrl/configuration.json").respondAsRated[Configuration]
  }

  /** Returns the list of languages supported by Twitter along with the language code supported by Twitter.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/developer-utilities/supported-languages/api-reference/get-help-languages" target="_blank">
    *   https://developer.twitter.com/en/docs/developer-utilities/supported-languages/api-reference/get-help-languages</a>.
    *
    * @return : The list of languages supported by Twitter.
    * */
  def supportedLanguages(): Future[RatedData[Seq[LanguageDetails]]] = {
    import restClient._
    Get(s"$helpUrl/languages.json").respondAsRated[Seq[LanguageDetails]]
  }

  /** Returns Twitter’s Privacy Policy.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/developer-utilities/privacy-policy/api-reference/get-help-privacy" target="_blank">
    *   https://developer.twitter.com/en/docs/developer-utilities/privacy-policy/api-reference/get-help-privacy</a>.
    *
    * @return : The Twitter's Privacy Policy.
    * */
  def privacyPolicy(): Future[RatedData[PrivacyPolicy]] = {
    import restClient._
    Get(s"$helpUrl/privacy.json").respondAsRated[PrivacyPolicy]
  }

  /** Returns the Twitter Terms of Service.
    * Note: these are not the same as the Developer Policy.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/developer-utilities/terms-of-service/api-reference/get-help-tos" target="_blank">
    *   https://developer.twitter.com/en/docs/developer-utilities/terms-of-service/api-reference/get-help-tos</a>.
    *
    * @return : the Twitter Terms of Service.
    * */
  def termsOfService(): Future[RatedData[TermsOfService]] = {
    import restClient._
    Get(s"$helpUrl/tos.json").respondAsRated[TermsOfService]
  }
}
