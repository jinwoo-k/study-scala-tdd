name := "scala-test-driven-development"

version := "0.1"

scalaVersion := "2.13.2"




libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.2" % Test,
  "io.cucumber" %% "cucumber-scala" % "6.1.1" % Test,
  "io.cucumber" % "cucumber-junit" % "6.1.1" % Test,
  "junit" % "junit" % "4.13" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test
)

resolvers ++= Seq(
  ("maven-central" at "https://repo1.maven.org/maven2/").withAllowInsecureProtocol(true)
)