import com.typesafe.sbt.SbtGit.{GitKeys => git}

enablePlugins(GhpagesPlugin, SiteScaladocPlugin)

name := "twitter4s"
version := "7.2-SNAPSHOT"

scalaVersion := "2.13.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.jcenterRepo
)

libraryDependencies ++= {
  val Typesafe = "1.4.2"
  val Akka = "2.6.19"
  val AkkaHttp = "10.2.9"
  val AkkaHttpJson4s = "1.39.2"
  val Json4s = "4.0.5"
  val Specs2 = "4.15.0"
  val ScalaLogging = "3.9.4"
  val RandomDataGenerator = "2.8"

  Seq(
    "com.typesafe" % "config" % Typesafe,
    "com.typesafe.akka" %% "akka-actor" % Akka,
    "com.typesafe.akka" %% "akka-stream" % Akka,
    "com.typesafe.akka" %% "akka-http" % AkkaHttp,
    "de.heikoseeberger" %% "akka-http-json4s" % AkkaHttpJson4s,
    "org.json4s" %% "json4s-native" % Json4s,
    "org.json4s" %% "json4s-ext" % Json4s,
    "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging,
    "org.specs2" %% "specs2-core" % Specs2 % "test",
    "org.specs2" %% "specs2-mock" % Specs2 % "test",
    "com.typesafe.akka" %% "akka-testkit" % Akka % "test",
    "com.danielasfregola" %% "random-data-generator" % RandomDataGenerator % "test"
  )
}

ThisBuild / scalacOptions ++= Seq("-language:postfixOps",
                                  "-language:implicitConversions",
                                  "-language:existentials",
                                  "-Xfatal-warnings",
                                  "-feature",
                                  "-deprecation:false")

lazy val standardSettings = Seq(
  organization := "com.danielasfregola",
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/DanielaSfregola/twitter4s")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/DanielaSfregola/twitter4s"),
            "scm:git:git@github.com:DanielaSfregola/twitter4s.git")),
  apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/latest/api/")),
  crossScalaVersions := Seq(scalaVersion.value, "2.12.15"),
  pomExtra :=
    <url>http://danielasfregola.github.io/twitter4s</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:github.com/DanielaSfregola/twitter4s</connection>
        <developerConnection>scm:git:git@github.com:DanielaSfregola/twitter4s</developerConnection>
        <url>github.com/DanielaSfregola/twitter4s</url>
      </scm>
      <developers>
        <developer>
          <id>DanielaSfregola</id>
          <name>Daniela Sfregola</name>
          <url>http://danielasfregola.com/</url>
        </developer>
      </developers>,
  publishMavenStyle := true,
  publishTo := {
    if (version.value.trim.endsWith("SNAPSHOT")) Some(Opts.resolver.sonatypeSnapshots)
    else Some(Opts.resolver.sonatypeStaging)
  },
  git.gitRemoteRepo := "git@github.com:DanielaSfregola/twitter4s.git",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-Xlint",
    "-deprecation:false",
    "-Xfatal-warnings",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
  ),
  Compile / doc / scalacOptions ++= Seq("-sourcepath", baseDirectory.value.getAbsolutePath),
  autoAPIMappings := true,
  apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/")),
  Compile / doc / scalacOptions ++= {
    val branch = if (version.value.trim.endsWith("SNAPSHOT")) "master" else version.value
    Seq(
      "-doc-source-url",
      "https://github.com/DanielaSfregola/twitter4s/tree/" + branch + "€{FILE_PATH}.scala"
    )
  }
)

lazy val coverageSettings = Seq(
  coverageExcludedPackages := "com.danielasfregola.twitter4s.processors.*;com.danielasfregola.twitter4s.Twitter*Client",
  coverageMinimum := 85
)

ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtTestOnCompile := true

SiteScaladoc / siteSubdirName := version + "/api"

lazy val root = (project in file("."))
  .settings(standardSettings ++ coverageSettings)
  .settings(
    name := "twitter4s"
  )
