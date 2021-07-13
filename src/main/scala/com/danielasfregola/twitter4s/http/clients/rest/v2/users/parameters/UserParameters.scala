package com.danielasfregola.twitter4s.http.clients.rest.v2.users.parameters

import com.danielasfregola.twitter4s.entities.v2.enums.expansions.UserExpansions.Expansions
import com.danielasfregola.twitter4s.entities.v2.enums.fields.TweetFields.TweetFields
import com.danielasfregola.twitter4s.entities.v2.enums.fields.UserFields.UserFields
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UserParameters(expansions: Seq[Expansions],
                                                   `tweet.fields`: Seq[TweetFields] = Seq.empty[TweetFields],
                                                   `user.fields`: Seq[UserFields] = Seq.empty[UserFields]) extends Parameters
