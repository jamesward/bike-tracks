name := "bike-tracks"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, ForcePlugin)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus" %% "play" % "1.1.0" % "test"
)

username in Force := sys.env.get("SALESFORCE_USERNAME").getOrElse("")

password in Force := sys.env.get("SALESFORCE_PASSWORD").getOrElse("")

unpackagedComponents in Force := Map("ApexClass" -> Seq("*"), "CustomTab" -> Seq("*"), "CustomObject" -> Seq("*"), "ApexPage" -> Seq("*"))