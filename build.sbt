name := "authentikat-jwt"

organization := "com.jason-goodwin"

scalaVersion := "2.10.1"

parallelExecution := false

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.9",
  "org.json4s" %% "json4s-native" % "3.2.8",
  "org.json4s" %% "json4s-jackson" % "3.2.8",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

credentials += Credentials(Path.userHome / ".mdialog.credentials")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "mDialog releases" at "http://mdialog.github.com/releases/"
)
