package com.danielasfregola.twitter4s.http.clients.rest.v2.users.deserialization

import com.danielasfregola.twitter4s.entities.RatedData
import com.danielasfregola.twitter4s.entities.v2.responses.{UserResponse, UsersResponse}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.TwitterUserLookupClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.deserialization.fixtures.{
  UserResponseFixture,
  UsersResponseFixture
}

class UsersDeserializationSpec extends ClientSpec {

  class UsersDeserializationClientSpec extends RestClientSpecContext with TwitterUserLookupClient

  "Twitter Clients" should {

    "deserialize UserResponses" in new UsersDeserializationClientSpec {
      val result: RatedData[UserResponse] = when(lookupUser("123"))
        .expectRequest(_ => {})
        .respondWithRated("/twitter/rest/v2/users/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === UserResponseFixture.fixture
    }

    "deserialize UsersResponses" in new UsersDeserializationClientSpec {
      val result: RatedData[UsersResponse] = when(lookupUsers(Seq("123")))
        .expectRequest(_ => {})
        .respondWithRated("/twitter/rest/v2/users/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === UsersResponseFixture.fixture
    }
  }
}
