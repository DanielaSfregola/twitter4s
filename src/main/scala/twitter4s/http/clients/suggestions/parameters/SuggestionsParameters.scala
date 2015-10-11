package twitter4s.http.clients.suggestions.parameters

import twitter4s.entities.enums.Language.Language
import twitter4s.http.marshalling.Parameters

case class SuggestionsParameters(lang: Language) extends Parameters
