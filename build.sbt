import com.typesafe.sbt.SbtGit.{GitKeys => git}

name := "twitter4s"
version := "5.3"

scalaVersion := "2.12.1"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  Resolver.jcenterRepo
)

libraryDependencies ++= {

  val Typesafe = "1.3.1"
  val Akka = "2.4.16"
  val AkkaHttp = "10.0.1"
  val AkkaHttpJson4s = "1.11.0"
  val Json4s = "3.5.0"
  val Spec2 = "3.8.6"
  val ScalaLogging = "3.5.0"

  Seq(
    "com.typesafe" % "config" % Typesafe,
    "com.typesafe.akka" %% "akka-http" % AkkaHttp,
    "de.heikoseeberger" %% "akka-http-json4s" % AkkaHttpJson4s,
    "org.json4s" %% "json4s-native" % Json4s,
    "org.json4s" %% "json4s-ext" % Json4s,
    "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging,
    "org.specs2" %% "specs2-core" % Spec2 % "test",
    "org.specs2" %% "specs2-mock" % Spec2 % "test",
    "com.typesafe.akka" %% "akka-testkit" % Akka % "test"
  )
}

scalacOptions in ThisBuild ++= Seq("-language:postfixOps",
  "-language:implicitConversions",
  "-language:existentials",
  "-feature",
  "-deprecation")

lazy val standardSettings = Seq(
  organization := "com.danielasfregola",
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/DanielaSfregola/twitter4s")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/DanielaSfregola/twitter4s"),
            "scm:git:git@github.com:DanielaSfregola/twitter4s.git")),
  apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/latest/api/")),
  crossScalaVersions := Seq("2.12.1", "2.11.8"),
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

lazy val coverageSettings = Seq(
  coverageExcludedPackages := "com.danielasfregola.twitter4s.processors.*;com.danielasfregola.twitter4s.Twitter*Client",
  coverageMinimum := 85
)

lazy val root = Project(
  id = "twitter4s",
  base = file("."),
  settings = standardSettings ++ coverageSettings ++ site.settings ++ site.includeScaladoc(version + "/api")
      ++ site.includeScaladoc("latest/api") ++ ghpages.settings)
