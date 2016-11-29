name := "twitter4s"

version := "2.1"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Spray Repository"    at "http://repo.spray.io",
  Resolver.jcenterRepo
)

libraryDependencies ++= {

  val Typesafe = "1.3.0"
  val Akka = "2.3.6"
  val Spray = "1.3.3"
  val Json4s = "3.2.11"
  val Spec2Version = "2.3.13"
  val ScalaLogging = "3.4.0"

  Seq(
    "com.typesafe" % "config" % Typesafe,
    "com.typesafe.akka" %% "akka-actor" % Akka,
    "io.spray" %% "spray-client" % Spray,
    "io.spray" %% "spray-http" % Spray,
    "io.spray" %% "spray-httpx" % Spray,
    "io.spray" %% "spray-routing" % Spray,
    "org.json4s" %% "json4s-native" % Json4s,
    "org.json4s" %% "json4s-ext" % Json4s,
    "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging,
    "org.specs2" %% "specs2-core" % Spec2Version % "test",
    "com.typesafe.akka" %% "akka-testkit" % Akka % "test"
  )
}
