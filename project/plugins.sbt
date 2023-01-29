addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")

addSbtPlugin("com.github.sbt" % "sbt-unidoc" % "0.5.0")

resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.0")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.3.2")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.17")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt-coursier" % "1.16")

Test / parallelExecution := false
