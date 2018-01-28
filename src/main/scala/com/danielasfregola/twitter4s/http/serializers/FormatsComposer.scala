package com.danielasfregola.twitter4s.http.serializers

import org.json4s.Formats

private[twitter4s] trait FormatsComposer {
  def compose(f: Formats): Formats
}
