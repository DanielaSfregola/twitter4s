import com.typesafe.sbt.SbtGhPages._
import com.typesafe.sbt.SbtGit.{GitKeys => git}
import com.typesafe.sbt.SbtSite._
import sbt.Keys._
import sbt.{LocalProject, _}
import scoverage.ScoverageSbtPlugin.ScoverageKeys._

object Resolvers {
  val resolvers = Seq(
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "Spray Repository"    at "http://repo.spray.io"
  )
}

object Dependencies {

  val TypesafeVersion = "1.3.0"
  val AkkaVersion = "2.3.6"
  val SprayVersion = "1.3.3"
  val Json4sVersion = "3.2.11"
  val Spec2Version = "2.3.13"

  val dependencies = Seq(
    "com.typesafe" % "config" % TypesafeVersion,
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "io.spray" %% "spray-client" % SprayVersion,
    "io.spray" %% "spray-http" % SprayVersion,
    "io.spray" %% "spray-httpx" % SprayVersion,
    "io.spray" %% "spray-routing" % SprayVersion,
    "org.json4s" %% "json4s-native" % Json4sVersion,
    "org.specs2" %% "specs2-core" % Spec2Version % "test",
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test"
  )
}

object Scoverage {

  val settings = Seq(
    coverageExcludedPackages := "com.danielasfregola.twitter4s.listeners.*"
  )

}

object Twitter4s extends Build {

  val v = "0.3-SNAPSHOT"

  lazy val standardSettings = Defaults.defaultSettings ++
  Seq(
    name := "twitter4s",
    version := v,
    scalaVersion := "2.11.7",
    organization := "com.danielasfregola",
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("https://github.com/DanielaSfregola/twitter4s")),
    scmInfo := Some(ScmInfo(url("https://github.com/DanielaSfregola/twitter4s"), "scm:git:git@github.com:DanielaSfregola/twitter4s.git")),
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
    resolvers ++= Resolvers.resolvers,

    publishMavenStyle := true,
    git.gitRemoteRepo := "git@github.com:DanielaSfregola/twitter4s.git",

    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-Xlint",
      "-deprecation",
      "-Xfatal-warnings",
      "-feature",
      "-language:postfixOps",
      "-unchecked"
    ),
    scalacOptions in (Compile, doc) <++= baseDirectory in LocalProject("twitter4s") map { bd =>
      Seq(
        "-sourcepath", bd.getAbsolutePath
      )
    },
    autoAPIMappings := true,
    apiURL := Some(url("http://DanielaSfregola.github.io/twitter4s/")),
    scalacOptions in (Compile, doc) <++= version in LocalProject("twitter4s") map { version =>
      val branch = if(version.trim.endsWith("SNAPSHOT")) "master" else version
      Seq[String](
          "-doc-source-url", "https://github.com/DanielaSfregola/twitter4s/tree/" + branch +"€{FILE_PATH}.scala"
        )
    }
  ) ++ site.settings ++ site.includeScaladoc(v +"/api") ++ site.includeScaladoc("latest/api") ++ ghpages.settings

  lazy val root = Project(id = "twitter4s",
    base = file("."),
    settings = standardSettings ++ Seq(
      libraryDependencies ++= Dependencies.dependencies
    ) ++ Scoverage.settings
  )
}
