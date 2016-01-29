name := "authentikat-jwt"

organization := "com.jason-goodwin"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", "2.11.7") //sbt '+ publish'

parallelExecution := true

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.9",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.bouncycastle" % "bcprov-jdk16" % "1.46"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % Test
)


resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://github.com/jason-goodwin.com/authentikat-jwt</url>
  <licenses>
    <license>
      <name>Apache2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:jasongoodwin/authentikat-jwt.git</url>
    <connection>scm:git:git@github.com:jasongoodwin/authentikat-jwt.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jasongoodwin</id>
      <name>Jason Goodwin</name>
      <url>http://refactoringfactory.wordpress.com</url>
    </developer>
  </developers>)


credentials += Credentials(Path.userHome / ".mvn.credentials") //TODO use these

credentials += Credentials("Sonatype Nexus Repository Manager",
                           "oss.sonatype.org",
                           "<your username>",
                           "<your password>")
