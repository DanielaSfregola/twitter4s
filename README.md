twitter4s
=========

[![Build Status](https://travis-ci.org/DanielaSfregola/twitter4s.svg?branch=master)](https://travis-ci.org/DanielaSfregola/twitter4s) [![codecov.io](http://codecov.io/github/DanielaSfregola/twitter4s/coverage.svg?branch=master)](http://codecov.io/github/DanielaSfregola/twitter4s?branch=master) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt) [![Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/twitter4s/Lobby)

An asynchronous non-blocking Scala Twitter Client, implemented using Akka-Http and json4s.

Prerequisites
-------------
 * JDK 8
 * Scala 2.11.+ and 2.12.+

- Go to http://apps.twitter.com/, login with your twitter account and register your application to get a consumer key and a consumer secret.
- Once the app has been created, generate a access key and access secret with the desired permission level.

Rate Limits
-----------
Be aware that the Twitter REST Api has rate limits specific to each endpoint. For more information, please have a look at the Twitter developers website [here](https://developer.twitter.com/en/docs/basics/rate-limiting).

For all the endpoints that are affected these limitations, information on the current rates together with the requested data is provided by the [RatedData](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/RatedData.html) case class.

Setup
-----
If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

```scala
resolvers += Resolver.sonatypeRepo("releases")
```

Also, you need to include the library as your dependency:
```scala
libraryDependencies += "com.danielasfregola" %% "twitter4s" % "5.5"
```

Giter8
------

If you are starting from scratch, you can use [`twitter4s.g8`](https://github.com/DanielaSfregola/twitter4s.g8) template to start your project. This template contains examples for both REST and Streaming client.

```bash
> sbt new DanielaSfregola/twitter4s.g8
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
These configurations will be automatically loaded when creating a twitter client, so all you have to do is to initialize your clients as following:
```scala
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.TwitterStreamingClient

val restClient = TwitterRestClient()
val streamingClient = TwitterStreamingClient()
```

Alternatively, you can also specify your tokens directly when creating the client:
```scala
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")  

val restClient = TwitterRestClient(consumerToken, accessToken)
val streamingClient = TwitterStreamingClient(consumerToken, accessToken)
```

Once you have instantiated your client you are ready to use it! :smile:

Twitter Streaming Client
-----------------------
[TwitterStreamingClient](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/TwitterStreamingClient) is the client to support stream connections offered by the Twitter Streaming Api.

You can initialize the client as follows:
```scala
import com.danielasfregola.twitter4s.TwitterStreamingClient

val client = TwitterStreamingClient()
```

There are three types of streams, each with different streaming message types: [Public Stream](https://github.com/DanielaSfregola/twitter4s#public-stream), [User Stream](https://github.com/DanielaSfregola/twitter4s#user-stream), [Site Stream](https://github.com/DanielaSfregola/twitter4s#site-stream).

Each stream requires a partial function that indicates how to process messages. If a message type is not specified, it is ignored. See the section of each stream for more information.

For example, you can create the following function to print the text of a tweet:
```scala
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

def printTweetText: PartialFunction[StreamingMessage, Unit] = {
    case tweet: Tweet => println(tweet.text)
}
```
All  you need to do is attach your processing function to the stream:
```scala
client.sampleStatuses(stall_warnings = true)(printTweetText)
```
...and you are done, happy days! :dancers:

Have a look at [TwitterProcessor](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/processors/TwitterProcessor.scala) for some predefined processing functions.

### Close or Replace a Stream
Each stream function returns a `Future[TwitterStream]`.
`TwitterStream` represents the stream received by Twitter and it can be used to close or replace the current stream.

For example, consider the following snippet:

```scala
  // TERRIBLE CODE! NEVER BLOCK! Code for demo purposes only!
  def simulateNextActionAfterMillis(millis: Long): Future[Unit] = Future{ Thread.sleep(millis); println() }

  for {
    streamA <- client.sampleStatuses(languages = Seq(Language.English)){ case t: Tweet => print("o")}
    _ <- simulateNextActionAfterMillis(10000)
    streamB <- streamA.sampleStatuses(languages = Seq(Language.Spanish)){ case t: Tweet => print("+")}
    _ <- simulateNextActionAfterMillis(10000)
  } yield streamB.close()
```

The above code can output something similar to the following:

 ```bash
 oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
 +++++++++++++++
 ```
In this example, we can see that there are more English tweets than Spanish tweets.

### Public Stream
Have a look at the complete scaladoc for the [Public Stream Client](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/streaming/statuses/TwitterStatusClient).

#### Available streams
- filterStatusesFilter
- sampleStatusesSample
- firehoseStatuses

#### CommonStreamingMessage types:
- [Tweet](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/Tweet)
- [DisconnectMessage](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/DisconnectMessage)
- [LimitNotice](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/LimitNotice)
- [LocationDeletionNotice](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/LocationDeletionNotice)
- [StatusDeletionNotice](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/StatusDeletionNotice)
- [StatusWithheldNotice](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/StatusWithheldNotice)
- [UserWithheldNotice](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/UserWithheldNotice)
- [WarningMessage](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/common/WarningMessage)

### User Stream
Have a look at the complete scaladoc for the [User Stream Client](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/streaming/users/TwitterUserClient).

#### Available streams
- userEvents

#### UserStreamingMessage types:
- All the instances of `CommonStreamingMessage` -- see the [Public Stream Section](https://github.com/DanielaSfregola/twitter4s#public-stream)
- [FriendsLists](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/user/FriendsLists) and [FriendsListsStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/user/FriendsListsStringified)
- [SimpleEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/user/SimpleEvent)
- [TweetEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/user/TweetEvent)
- [TwitterListEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/user/TwitterListEvent)

### Site Stream
Have a look at the complete scaladoc for the [Site Stream Client](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/streaming/sites/TwitterSiteClient).

#### Available streams
- siteEvents

#### SiteStreamingMessage types:
- All the `CommonStreamingMessage`s -- see the [Public Stream Section](https://github.com/DanielaSfregola/twitter4s#public-stream)
- [ControlMessage](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/ControlMessage)
- [UserEnvelopTweet](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTweet) and [UserEnvelopTweetStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTweetStringified)
- [UserEnvelopDirectMessage](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopDirectMessage) and [UserEnvelopDirectMessageStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopDirectMessageStringified)
- [UserEnvelopSimpleEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopSimpleEvent) and [UserEnvelopSimpleEventStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopSimpleEventStringified)
- [UserEnvelopTweetEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTweetEvent) and [UserEnvelopTweetEventStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTweetEventStringified)
- [UserEnvelopTwitterListEvent](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTwitterListEvent) and [UserEnvelopTwitterListEventStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopTwitterListEventStringified)
- [UserEnvelopFriendsLists](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopFriendsLists) and [UserEnvelopFriendsListsEventStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopFriendsListsStringified)
- [UserEnvelopWarningMessage](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopWarningMessage) and [UserEnvelopWarningMessageStringified](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/entities/streaming/site/UserEnvelopWarningMessageStringified)

### Documentation
The complete scaladoc with all the available streams for the `TwitterStreamingClient` can be found [here](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/TwitterStreamingClient).


Twitter REST Client
-------------------

[TwitterRestClient](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/TwitterRestClient) is the client for the REST endpoints offered by the Twitter REST Api.

Once you have configured your consumer and access token, you can initialize an instance of `TwitterRestClient` as follows:
```scala
import com.danielasfregola.twitter4s.TwitterRestClient

val client = TwitterRestClient()
```

For example, you can get the home timeline of the authenticated user:
```scala
client.homeTimeline()
```

or you can get the timeline of a specific user:
```scala
client.userTimelineForUser("DanielaSfregola")
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

### Documentation
The complete scaladoc with all the available functionalities for the `TwitterRestClient` can be found [here](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/TwitterRestClient).

[TwitterRestClient](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/TwitterRestClient) is composed by several traits. A list of the supported resources is following:
- [account](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/account/TwitterAccountClient)
- [application](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/application/TwitterApplicationClient)
- [blocks](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/blocks/TwitterBlockClient)
- [direct_messages](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/directmessages/TwitterDirectMessageClient)
- [favorites](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/favorites/TwitterFavoriteClient)
- [followers](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/followers/TwitterFollowerClient)
- [friends](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/friends/TwitterFriendClient)
- [friendships](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/friendships/TwitterFriendshipClient)
- [geo](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/geo/TwitterGeoClient)
- [help](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/help/TwitterHelpClient)
- [lists](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/lists/TwitterListClient)
- [mutes](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/mutes/TwitterMuteClient)
- [saved_searches](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/savedsearches/TwitterSavedSearchClient)
- [searches](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/search/TwitterSearchClient)
- [statuses](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/statuses/TwitterStatusClient)
- [suggestions](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/suggestions/TwitterSuggestionClient)
- [users](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/users/TwitterUserClient)
- [trends](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/trends/TwitterTrendClient)
- [media](http://danielasfregola.github.io/twitter4s/5.5/api/com/danielasfregola/twitter4s/http/clients/rest/media/TwitterMediaClient)

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

Logging
-------
Twitter4s uses [scala-logging](https://github.com/typesafehub/scala-logging) and can be used in your `twitter4s` application.

In your application you will need a logging backend (logback, logstash). [logback-classic](https://github.com/qos-ch/logback) is easy to use and will suit most needs.
You can find a sample configuration in [twitter4s-demo](https://github.com/DanielaSfregola/twitter4s-demo) and in the Giter8 template [twitter4s.g8](https://github.com/DanielaSfregola/twitter4s.g8)

Examples
--------
Have a look at the repository [twitter4s-demo](https://github.com/DanielaSfregola/twitter4s-demo) for more examples on how to use `twitter4s`.

Snapshot Versions
-----------------
To use a snapshot version of this library, make sure you have the resolver for maven central (snapshot repositories) in your SBT settings:
```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

Then, add the library as your dependency:
```scala
libraryDependencies += "com.danielasfregola" %% "twitter4s" % "5.6-SNAPSHOT"
```

Coming up Features
---------------
- OAuth1 support
- Advanced query support
- Support for dump to file
- ...

How to Contribute
-----------------
Contributions and feature requests are always welcome!

- Fork the repo and checkout the code
- Make sure to run sbt with jdk8+
- Run the tests with `sbt test`
- ...you can now do your magic and submit a PR when you are ready! :dancers:
