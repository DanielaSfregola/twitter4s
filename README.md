twitter4s
=========

[![Build Status](https://travis-ci.org/DanielaSfregola/twitter4s.svg?branch=master)](https://travis-ci.org/DanielaSfregola/twitter4s) [![Coverage Status](https://img.shields.io/coveralls/DanielaSfregola/twitter4s.svg)](https://coveralls.io/r/DanielaSfregola/twitter4s?branch=master) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

An asynchronous non-blocking Scala Twitter Client, implemented using spray and json4s.

Prerequisites
-------------
Scala 2.11.+ is supported.

- Go to http://apps.twitter.com/, login with your twitter account and register your application to get a consumer key and a consumer secret.
- Once the app has been created, generate a access key and access secret with the desired permission level.

Rate Limits
-----------
Be aware that the Twitter API has rate limits specific to each endpoint. For more information, please have a look at the Twitter developers website [here](https://dev.twitter.com/rest/public/rate-limits).

Setup
-----
If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

```scala
resolver += Resolver.sonatypeRepo("releases")
```

Also, you need to include the library as your dependency:
```scala
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "1.0"
)
```

Usage
-----

Add your consumer and access token as either environment variables or as part of your configuration.
Twitter4s will look for the following environment variables:
```bash
export TWITTER_CONSUMER_TOKEN_KEY='my-consumer-key'
export TWITTER_CONSUMER_TOKEN_SECRET='my-consumer-secret'
export TWITTER_ACCESS_TOKEN_KEY='my-access-key'
export TWITTER_ACCESS_TOKEN_SECRET='my-access-secret'
```
You can also add them to your configuration file, usually called `application.conf`:
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
These configurations will be automatically loaded when creating a twitter client.
Alternatively, you can also specify your tokens directly when creating the client:
```scala
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")  

val restClient = new TwitterRestClient(consumerToken, accessToken)
val streamingClient = new TwitterStreamingClient(consumerToken, accessToken)
```

Once you have instantiated your client you are ready to use it! :smile:

Twitter Streaming Client
-----------------------
[TwitterStreamingClient](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/TwitterStreamingClient.scala) is the client for the [Twitter Streaming API](https://dev.twitter.com/streaming/overview).

You can initialize the client as following:
```scala
import com.danielasfregola.twitter4s.TwitterStreamingClient

val client = TwitterStreamingClient()
```
Each stream will require a function `f: StreamingMessage => Unit` that defines which messages to process and how to process them.

For example, you can create the following function to print the text of a tweet:
```scala
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

def printTweetText(msg: StreamingMessage): Unit =
  msg match {
    case tweet: Tweet => print(tweet.text)
    case _ =>
  }
```
All  you need to do is attach your processing function to the stream:
```scala
client.getStatusesSample(stall_warnings = true)(printTweetText)
```
...and you are done, happy days! :dancers:

Have a look at [TwitterProcessor](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/processors/TwitterProcessor.scala) for some predefined processing functions.

### Documentation
The complete scaladoc with all the available streams for the `TwitterStreamingClient` can be found [here](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.TwitterStreamingClient).

Following is a list of all the available `StreamingMessage` instances:
- [ControlMessage](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/ControlMessage.scala)
- [DirectMessage](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/DirectMessage.scala)
- [DisconnectMessage](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/DisconnectMessage.scala)
- [Event](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/Event.scala)
- [FriendsLists](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/FriendsLists.scala) and [FriendsListsStringified](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/FriendsLists.scala)
- [LimitNotice](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/LimitNotice.scala)
- [LocationDeletionNotice](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/LocationDeletionNotice.scala)
- [StatusDeletionNotice](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/StatusDeletionNotice.scala)
- [StatusWithheldNotice](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/StatusWithheldNotice.scala)
- [Tweet](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/Tweet.scala)
- [UserEnvelop](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/UserEnvelop.scala) and [UserEnvelopStringified](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/UserEnvelop.scala)
- [UserWithheldNotice](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/UserWithheldNotice.scala)
- [WarningMessage](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/streaming/WarningMessage.scala)

Also, all the `EventTargetObject` instances are:
- [Tweet](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/Tweet.scala)
- [TweetList](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/TwitterList.scala)
- [DirectMessage](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/DirectMessage.scala)


Twitter REST Client
-------------------

[TwitterRestClient](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/TwitterRestClient.scala) is the client for the [Twitter REST API](https://dev.twitter.com/rest/public).

You can initialize the client as following:
```
import com.danielasfregola.twitter4s.TwitterRestClient

val client = TwitterRestClient()
```

For example, you can get the home timeline of the authenticated user:
```scala
client.getHomeTimeline()
```

or you can get the timeline of a specific user:
```scala
client.getUserTimelineForUser("DanielaSfregola")
```

You can also update your tweet status:
```scala
client.tweet(status = "Test")
```
Asynchronous upload of images or short videos is also supported:
```scala
for {
  upload <- client.uploadMediaFromPath("/path/to/file.png")
  tweet <- client.tweet(status = "Test with media", media_ids = Seq(upload.media_id))
} yield tweet
```

### Examples
Have a look at the repository [twitter4s-demo](https://github.com/DanielaSfregola/twitter4s-demo) for more examples on how to use this library.
Also, a completed list of the supported functionalities is provided in the [Documentation](https://github.com/DanielaSfregola/twitter4s#documentation) section.

### Documentation
The complete scaladoc with all the available functionalities for the `TwitterRestClient` can be found [here](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.TwitterRestClient).

[TwitterRestClient](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.TwitterRestClient) is composed by several traits. A list of the supported resources is following:
- [account](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.account.TwitterAccountClient)
- [application](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.application.TwitterApplicationClient)
- [blocks](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.blocks.TwitterBlockClient)
- [direct_messages](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.directmessages.TwitterDirectMessageClient)
- [favorites](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.favorites.TwitterFavoriteClient)
- [followers](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.followers.TwitterFollowerClient)
- [friends](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.friends.TwitterFriendClient)
- [friendships](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.friendships.TwitterFriendshipClient)
- [geo](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.geo.TwitterGeoClient)
- [help](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.help.TwitterHelpClient)
- [lists](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.lists.TwitterListClient)
- [mutes](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.mutes.TwitterMuteClient)
- [saved_searches](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.savedsearches.TwitterSavedSearchClient)
- [searches](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.search.TwitterSearchClient)
- [statuses](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.statuses.TwitterStatusClient)
- [suggestions](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.suggestions.TwitterSuggestionClient)
- [users](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.users.TwitterUserClient)
- [trends](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.trends.TwitterTrendClient)
- [media](http://danielasfregola.github.io/twitter4s/latest/api/index.html#com.danielasfregola.twitter4s.http.rest.clients.media.TwitterMediaClient)

Proxy Support
-------------
If needed, you can redefine the domain used for each of the twitter api by overriding the following settings in your configuration file:
```scala
twitter {

  rest {
    api = "https://api.twitter.com"
    media = "https://upload.twitter.com"
  }

  streaming {
    public = "https://stream.twitter.com"
    user = "https://userstream.twitter.com"
    site = "https://sitestream.twitter.com"
  }
}
```

Coming up Features
---------------
- Efficient Login
- Query support
- Site streaming extended support
- Support for dump to file
- Upgrade to Akka Http
- ...

Contributions and feature requests are always welcome!

Previous Versions
-----------------
For document of versions previous to 1.0, please have a look at [0.2.1-README.md](https://github.com/DanielaSfregola/twitter4s/blob/master/0.2.1-README.md).

Snapshot Versions
-----------------
To use a snapshot version of this library, make sure you have the resolver for maven central (snapshot repositories) in your SBT settings:
```scala
resolver += Resolver.sonatypeRepo("snapshots")
```

Then, add the library as your dependency:
```scala
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "1.1-SNAPSHOT"
)
```
