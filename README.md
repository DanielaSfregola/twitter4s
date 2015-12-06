twitter4s
=========

[![Build Status](https://travis-ci.org/DanielaSfregola/twitter4s.svg?branch=master)](https://travis-ci.org/DanielaSfregola/twitter4s) [![Coverage Status](https://img.shields.io/coveralls/DanielaSfregola/twitter4s.svg)](https://coveralls.io/r/DanielaSfregola/twitter4s?branch=master) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

An asynchronous non-blocking Scala Twitter Client, implemented using spray and json4s.

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
If you use SBT, if you don't have it already, make sure you add the Maven Central as resolver:

```scala
resolvers += "Maven central" at "http://repo1.maven.org/maven2/"
```

Also, you need to include the library as your dependency:
```scala
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "0.1"
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

Alternatively, you can also specify your tokens directly when creating the client:
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
Have a look at the repository [twitter4s-demo](https://github.com/DanielaSfregola/twitter4s-demo) for some examples on how to use this library.
Also, have a completed list of the supported functionalities is provided in the [Documentation](https://github.com/DanielaSfregola/twitter4s#documentation) section.

Documentation
-------------
The complete scaladoc with all the available functionalities can be found [here](http://danielasfregola.github.io/twitter4s).

[TwitterClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.TwitterClient) is composed by several traits. A list of the supported resources is following:
- [account](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.account.TwitterAccountClient)
- [application](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.application.TwitterApplicationClient)
- [blocks](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.blocks.TwitterBlockClient)
- [direct_messages](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.directmessages.TwitterDirectMessageClient)
- [favorites](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.favorites.TwitterFavoriteClient)
- [followers](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.followers.TwitterFollowerClient)
- [friends](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.friends.TwitterFriendClient)
- [friendships](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.friendships.TwitterFriendshipClient)
- [geo](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.geo.TwitterGeoClient)
- [help](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.help.TwitterHelpClient)
- [lists](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.lists.TwitterListClient)
- [mutes](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.mutes.TwitterMuteClient)
- [saved_searches](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.savedsearches.TwitterSavedSearchClient)
- [searches](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.search.TwitterSearchClient)
- [statuses](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.statuses.TwitterStatusClient)
- [suggestions](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.suggestions.TwitterSuggestionClient)
- [users](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.users.TwitterUserClient)
- [trends](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.clients.trends.TwitterTrendClient)

Coming up Features
---------------
- Streaming support
- Media support
- Efficient Login and token management
- Query support
- ...

Contributions and feature requests are more than welcome!
