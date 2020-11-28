ThisBuild / organization := "com.jason-goodwin"
ThisBuild / organizationName := "Github"
ThisBuild / organizationHomepage := Some(url("http://refactoringfactory.wordpress.com"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/jasongoodwin/authentikat-jwt"),
    "scm:git@github.com:jasongoodwin/authentikat-jwt.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "jasongoodwin",
    name  = "Jason Goodwin",
    email = "jasongoodwin@gmail.com",
    url   = url("http://refactoringfactory.wordpress.com")
  )
)

ThisBuild / description := "Some Claims Based JWT Implementation for Scala"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/jasongoodwin/authentikat-jwt"))

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true