package twitter4s.http.clients.application

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.RateLimits
import twitter4s.entities.enums.Resource
import twitter4s.util.{ClientSpecContext, ClientSpec}

class TwitterApplicationClientSpec  extends ClientSpec {

  trait TwitterApplicationClientSpecContext extends ClientSpecContext with TwitterApplicationClient

  "Twitter Friend Client" should {

    "get friends ids of a specific user by id" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(rateLimits(Resource.Account, Resource.Statuses)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.query === Query("resources=account,statuses")
      }.respondWith("/twitter/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/application/rate_limits.json")
    }
  }
}
