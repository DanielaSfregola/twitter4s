twitter4s
=========

[![Build Status](https://travis-ci.org/DanielaSfregola/twitter4s.svg?branch=master)](https://travis-ci.org/DanielaSfregola/twitter4s) [![Coverage Status](https://img.shields.io/coveralls/DanielaSfregola/twitter4s.svg)](https://coveralls.io/r/DanielaSfregola/twitter4s?branch=master) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

An asynchronous non-blocking Scala client for the Twitter API.

Prerequisites
-------------
Only Scala 2.11.+ is supported.

- Go to http://apps.twitter.com/, login with your twitter account and register your application to get a consumer key and a consumer secret.
- Once the app has been created, generate a access key and access secret with the desired permission level.

Rate Limits
-----------
Be aware that the Twitter API has rate limits specific to each endpoint. For more information, please have a look at the Twitter developers website [here](https://dev.twitter.com/rest/public/rate-limits).

Setup
-----
If you use SBT, if you don't have it already, make sure you add the Sonatype repo as resolver:

```scala
resolvers += "Sonatype OSS Releases" at
 "https://oss.sonatype.org/content/repositories/releases/"
```

Also, you need to include the library as your dependency:
```scala
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "0.1-SNAPSHOT"
)
```

Usage
-----
Add your consumer and access token to your configuration file, usually `application.conf` file:
```scala
twitter {
  consumer {
    key = "my-consumer-key"
    secret = "my-consumer-secret"
  }
  access {
    key = "my-access-key"
    secret = "my-access-secret"
  }
}
```

These configurations will be automatically loaded when creating the twitter client:
```scala
import com.danielasfregola.twitter4s.TwitterClient

val client = new TwitterClient()
```

Alternatively, you can specify you token directly when creating the client:
```scala
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")  
val client = new TwitterClient(consumerToken, accessToken)
```
Once you have instanced your client you are ready to use it! :smile:

For example, you can get the home timeline of the authenticated user:
```scala
client.getHomeTimeline()
```

or you can get the timeline of a specific user:
```scala
client.getUserTimelineForUser("DanielaSfregola")
```

Have a look at the documentation section for the list of supported functionalities.

Documentation
-------------
The complete scaladoc with all the available functionalities can be found [here](http://danielasfregola.github.io/twitter4s).

[TwitterClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.TwitterClient) is composed by several traits grouped by resources and functionalities:
- [TwitterAccountClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.account.TwitterAccountClient)
- [TwitterApplicationClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.application.TwitterApplicationClient)
- [TwitterBlockClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.blocks.TwitterBlockClient)
- [TwitterDirectMessageClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.directmessages.TwitterDirectMessageClient)
- [TwitterFavoriteClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.favorites.TwitterFavoriteClient)
- [TwitterFollowerClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.followers.TwitterFollowerClient)
- [TwitterFriendClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.friends.TwitterFriendClient)
- [TwitterFriendshipClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.friendships.TwitterFriendshipClient)
- [TwitterGeoClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.geo.TwitterGeoClient)
- [TwitterHelpClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.help.TwitterHelpClient)
- [TwitterListClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.lists.TwitterListClient)
- [TwitterMuteClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.mutes.TwitterMuteClient)
- [TwitterSavedSearchClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.savedsearches.TwitterSavedSearchClient)
- [TwitterSearchClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.search.TwitterSearchClient)
- [TwitterStatusClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.statuses.TwitterStatusClient)
- [TwitterSuggestionClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.suggestions.TwitterSuggestionClient)
- [TwitterUserClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.users.TwitterUserClient)
- [TwitterTrendClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.trends.TwitterTrendClient)

Coming up Features
---------------
- Streaming support
- Media support
- Efficient Login and token management
- ...

Contributions and requests are more than welcome!
