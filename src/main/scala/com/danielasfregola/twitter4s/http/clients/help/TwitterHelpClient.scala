package com.danielasfregola.twitter4s.http.clients.help

import scala.concurrent.Future
import com.danielasfregola.twitter4s.entities.{LanguageDetails, TermsOfService, PrivacyPolicy, Configuration}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterHelpClient extends OAuthClient with Configurations {

  private val helpUrl = s"$apiTwitterUrl/$twitterVersion/help"

  def configuration(): Future[Configuration] =
    Get(s"$helpUrl/configuration.json").respondAs[Configuration]
  
  def supportedLanguages(): Future[Seq[LanguageDetails]] =
    Get(s"$helpUrl/languages.json").respondAs[Seq[LanguageDetails]]

  def privacyPolicy(): Future[PrivacyPolicy] =
    Get(s"$helpUrl/privacy.json").respondAs[PrivacyPolicy]

  def termsOfService(): Future[TermsOfService] =
    Get(s"$helpUrl/tos.json").respondAs[TermsOfService]
}
