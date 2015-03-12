name := "bike-tracks"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus" %% "play" % "1.1.0" % "test"
)
