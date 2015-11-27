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
  "com.danielasfregola.twitter4s" %% "twitter4s" % "0.1-SNAPSHOT"
)
```

Usage
-----
TODO

Documentation
-------------
TODO

Coming up Features
---------------
- Streaming support
- Media support
- Efficient Login and token management
- ...more to come!
