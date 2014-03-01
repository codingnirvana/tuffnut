import sbt._
import sbt.Keys._

object TuffNutBuild extends Build {

  import Resolvers._
  import Dependencies._

  lazy val tuffnut = Project(
    id = "tuffnut",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "tuffnut",
      organization := "indix",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      resolvers := Seq(sonatypeRepo, typesafeRepo, mavenRepo),
      libraryDependencies ++= Seq(dropwizardScala, jackson, jsoup, commonsIO, dispatch,
      scalaTest, mockito, jodaConvert, jodaTime, commonsLang, commonsCodec,
      crawlerCommons)
    )
  )

  object Resolvers {
    val sonatypeRepo = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"
    val typesafeRepo = "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
    val mavenRepo = "Maven Central" at "http://repo1.maven.org/maven2/"

  }

  object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val mockito = "org.mockito" % "mockito-core" % "1.8.5" % "test"
  val dropwizardScala = "com.massrelevance" % "dropwizard-scala_2.10" % "0.6.2" exclude("org.slf4j", "log4j-over-slf4j")
val jodaConvert = "org.joda" % "joda-convert" % "1.2"
  val jodaTime = "joda-time" % "joda-time" % "2.2"
  val jackson = "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13"
  val jsoup = "org.jsoup" % "jsoup" % "1.7.1"
  val commonsLang = "org.apache.commons" % "commons-lang3" % "3.2.1"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.8"
  val dispatch ="net.databinder.dispatch" %% "dispatch-core" % "0.10.1"
  val commonsIO = "commons-io" % "commons-io" % "2.4"
  val crawlerCommons = "com.google.code.crawler-commons" % "crawler-commons" % "0.3"
  }


}
