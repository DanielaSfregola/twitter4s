import com.typesafe.sbt.SbtGit.{GitKeys => git}
import scoverage.ScoverageSbtPlugin.ScoverageKeys._

name := "twitter4s"
version := "3.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Spray Repository" at "http://repo.spray.io",
  Resolver.jcenterRepo
)

libraryDependencies ++= {

  val Typesafe = "1.3.0"
  val Akka = "2.4.11"
  val Spray = "1.3.3"
  val Json4s = "3.2.11"
  val Spec2Version = "2.3.13"
  val ScalaLogging = "3.4.0"

  Seq(
    "com.typesafe" % "config" % Typesafe,
    "com.typesafe.akka" %% "akka-actor" % Akka,
    "com.typesafe.akka" %% "akka-http-experimental" % Akka,
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

coverageExcludedPackages := "com.danielasfregola.twitter4s.processors.*;com.danielasfregola.twitter4s.Twitter*Client"
coverageMinimum := 85

lazy val standardSettings = Seq(
  organization := "com.danielasfregola",
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/DanielaSfregola/twitter4s")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/DanielaSfregola/twitter4s"),
            "scm:git:git@github.com:DanielaSfregola/twitter4s.git")),
  apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/latest/api/")),
  pomExtra := (
    <developers>
    <developer>
      <id>DanielaSfregola</id>
      <name>Daniela Sfregola</name>
      <url>http://danielasfregola.com/</url>
    </developer>
  </developers>
  ),
  publishMavenStyle := true,
  git.gitRemoteRepo := "git@github.com:DanielaSfregola/twitter4s.git",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-Xlint",
    "-deprecation",
    "-Xfatal-warnings",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
  ),
  scalacOptions in (Compile, doc) ++= Seq("-sourcepath", baseDirectory.value.getAbsolutePath),
  autoAPIMappings := true,
  apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/")),
  scalacOptions in (Compile, doc) ++= {
    val branch = if (version.value.trim.endsWith("SNAPSHOT")) "master" else version.value
    Seq(
      "-doc-source-url",
      "https://github.com/DanielaSfregola/twitter4s/tree/" + branch + "€{FILE_PATH}.scala"
    )
  }
)

lazy val root = Project(
  id = "twitter4s",
  base = file("."),
  settings = standardSettings ++ site.settings ++ site.includeScaladoc(version + "/api")
      ++ site.includeScaladoc("latest/api") ++ ghpages.settings)
