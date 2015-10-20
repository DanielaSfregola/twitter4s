package twitter4s.http.clients.help

import scala.concurrent.Future

import twitter4s.LanguageDetails
import twitter4s.entities.{TermsOfService, PrivacyPolicy, Configuration}
import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterHelpClient extends OAuthClient with Configurations {

  val helpUrl = s"$apiTwitterUrl/$twitterVersion/help"

  def configuration(): Future[Configuration] =
    Get(s"$helpUrl/configuration.json").respondAs[Configuration]
  
  def supportedLanguages(): Future[Seq[LanguageDetails]] =
    Get(s"$helpUrl/languages.json").respondAs[Seq[LanguageDetails]]

  def privacyPolicy(): Future[PrivacyPolicy] =
    Get(s"$helpUrl/privacy.json").respondAs[PrivacyPolicy]

  def termsOfService(): Future[TermsOfService] =
    Get(s"$helpUrl/tos.json").respondAs[TermsOfService]
}
