name := "twitter4s"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Spray Repository"    at "http://repo.spray.io"
)

libraryDependencies ++= {

  val TypesafeVersion = "1.3.0"
  val AkkaVersion = "2.3.6"
  val SprayVersion = "1.3.3"
  val Json4sVersion = "3.2.11"
  val Spec2Version = "2.3.13"

  Seq(
    "com.typesafe" % "config" % TypesafeVersion,
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "io.spray" %% "spray-client" % SprayVersion,
    "io.spray" %% "spray-http" % SprayVersion,
    "io.spray" %% "spray-routing" % SprayVersion,
    "org.json4s" %% "json4s-native" % Json4sVersion,
    "org.specs2" %% "specs2-core" % Spec2Version % "test",
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test"
  )
}
