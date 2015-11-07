package com.danielasfregola.twitter4s.http.clients.suggestions.parameters

import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class SuggestionsParameters(lang: Language) extends Parameters
