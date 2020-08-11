name := "scala-test-driven-development"

version := "0.1"

scalaVersion := "2.11.8"

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "test",
  "org.scalatest" %% "scalatest" % "3.1.2" % Test,
  "io.cucumber" %% "cucumber-scala" % "6.1.1" % Test,
  "io.cucumber" % "cucumber-junit" % "6.1.1" % Test,
  "junit" % "junit" % "4.13" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "joda-time" % "joda-time" % "2.9.2"
)

resolvers ++= Seq(
  ("maven-central" at "https://repo1.maven.org/maven2/").withAllowInsecureProtocol(true)
)