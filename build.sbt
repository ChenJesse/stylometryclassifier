name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "org.scalanlp" %% "breeze" % "0.13"
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14"
libraryDependencies += "org.scalanlp" % "epic-parser-en-span_2.11" % "2016.8.29" exclude("org.slf4j", "slf4j-simple")



