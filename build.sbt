import sbt.Keys.useCoursier

name := "authentikat-jwt"
crossScalaVersions := Seq("2.11.11", "2.12.10", "2.13.3")

inThisBuild(
  List(
    scalaVersion := "2.13.3", // 2.12.10, or 2.11.11
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    parallelExecution := false,
    useCoursier := true
  )
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Ywarn-unused"
)

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.15",
  "org.json4s" %% "json4s-native" % "3.7.0-M7",
  "org.json4s" %% "json4s-jackson" % "3.7.0-M7",
  "org.scalatest" %% "scalatest" % "3.2.3" % Test
)