name := """rest-swagger-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  "joda-time" % "joda-time" % "2.3",
  "org.xerial" % "sqlite-jdbc" % "3.8.6"
  ,"com.typesafe.play" %% "play-slick" % "1.1.1"
  ,"com.typesafe.play" %% "play-slick-evolutions" % "1.1.1"

  // 2.5 play ,"com.typesafe.play" %% "play-slick" % "2.0.0"
  // 2.5 play //,"com.typesafe.play" %% "play-slick-evolutions" % "2.0.0"

  //,"io.swagger" %% "swagger-play2" % "1.6.0"
  ,"io.swagger" %% "swagger-play2" % "1.5.1"
  ,"org.webjars" % "swagger-ui" % "2.1.4"
  ,"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2"

)

libraryDependencies += evolutions

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


