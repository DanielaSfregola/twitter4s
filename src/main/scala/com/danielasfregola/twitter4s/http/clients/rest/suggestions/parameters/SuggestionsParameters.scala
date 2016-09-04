package com.danielasfregola.twitter4s.http.clients.rest.suggestions.parameters

import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class SuggestionsParameters(lang: Language) extends Parameters
