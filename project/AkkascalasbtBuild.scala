import sbt._
import sbt.Keys._

object AkkascalasbtBuild extends Build {

  import Resolvers._
  import Dependencies._

  lazy val akkascalasbt = Project(
    id = "akka-code-samples",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "akka-code-samples",
      organization := "indix",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      resolvers := Seq(sonatypeRepo, typesafeRepo),
      libraryDependencies ++= Seq(
        akkaActor, akkaRemote
      )
    )
  )

  object Resolvers {
    val sonatypeRepo = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"
    val typesafeRepo = "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
  }

  object Dependencies {
    val akkaVersion: String = "2.2.1"
    val akkaActor = "com.typesafe.akka" % "akka-actor_2.10" % akkaVersion
    val akkaRemote = "com.typesafe.akka" % "akka-remote_2.10" % akkaVersion
  }

}
