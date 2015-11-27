twitter4s
=========

[![Build Status](https://travis-ci.org/DanielaSfregola/twitter4s.svg?branch=master)](https://travis-ci.org/DanielaSfregola/twitter4s) [![Coverage Status](https://img.shields.io/coveralls/DanielaSfregola/twitter4s.svg)](https://coveralls.io/r/DanielaSfregola/twitter4s?branch=master) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

An asynchronous non-blocking Scala client for the Twitter API.

Prerequisites
-------------
- Go to http://apps.twitter.com/, login with your twitter account and register your application to get a consumer key and a consumer secret.
- Once the app has been created, generate a access key and access secret with the desired permission level.

Also, note that the moment, only Scala 2.11.+ is supported.

Setup
-----
If you use SBT, make sure you have the Sonatype repo as resolver:

```
resolvers += "Sonatype OSS Releases" at
 "https://oss.sonatype.org/content/repositories/releases/"
```

Also, you need to include the library as your dependency:
```
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "0.1-SNAPSHOT"
)
```

Usage
-----
Add your consumer and access token to your configuration file, usually `application.conf` file:
```
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
```
import com.danielasfregola.twitter4s.TwitterClient

val client = new TwitterClient()
```

Alternatively, you can specify you token directly when creating the client:
```
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")  
val client = new TwitterClient(consumerToken, accessToken)
```
Once you have instanced your client you are ready to use it! :smile:

For example, you can get the home timeline of the authenticated user:
```
client.getHomeTimeline()
```
or you can get the timeline of a specific user:
```
client.getUserTimelineForUser("DanielaSfregola")
```

Have a look at the documentation section for the list of supported methods.

Documentation
-------------
TODO

Coming up Features
---------------
- Streaming support
- Media support
- Efficient Login and token management
- ...more to come!
