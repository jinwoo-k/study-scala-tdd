name := "study-scala-tdd"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test" // 3.2.0
libraryDependencies +="info.cukes" %% "cucumber-scala" % "1.2.4" % "test"
libraryDependencies +="info.cukes" % "cucumber-junit" % "1.2.4" % "test"
// http://jmock.org/
libraryDependencies += "org.jmock" % "jmock-junit4" % "2.11.0" % Test
// https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core
libraryDependencies += "org.hamcrest" % "hamcrest-core" % "1.3" % Test
// https://mvnrepository.com/artifact/org.hamcrest/hamcrest-library
libraryDependencies += "org.hamcrest" % "hamcrest-library" % "1.3" % Test
// for scalacheck (property based check -> Generator-driven properties)

// https://stackoverflow.com/questions/35312410/java-lang-incompatibleclasschangeerror-implementing-class-with-scalacheck-and-s
  // ScalaTest 2.2.6 is incompatible with ScalaCheck 1.13.0. Downgrading to "scalacheck" % "1.12.5" helps.
//libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.5" % Test
