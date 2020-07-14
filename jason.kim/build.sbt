name := "scala-test-driven-development"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % Test
libraryDependencies += "io.cucumber" %% "cucumber-scala" % "6.1.1" % Test
libraryDependencies += "io.cucumber" % "cucumber-junit" % "6.1.1" % Test
libraryDependencies += "junit" % "junit" % "4.13" % Test

resolvers ++= Seq(
  ("maven-central" at "https://repo1.maven.org/maven2/").withAllowInsecureProtocol(true)
)