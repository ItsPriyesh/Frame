import com.mojolly.scalate.ScalatePlugin.ScalateKeys._
import com.mojolly.scalate.ScalatePlugin._
import org.scalatra.sbt._
import sbt.Keys._
import sbt._

object FrameBuild extends Build {
  val Organization = "me.priyesh"
  val Name = "Frame"
  val Version = "0.1"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC3"

  lazy val project = Project (
    "frame",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
        "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
