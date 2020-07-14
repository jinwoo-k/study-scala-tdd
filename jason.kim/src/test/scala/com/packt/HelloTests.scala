package com.packt

import java.io.File

import org.scalatest.{BeforeAndAfter, ParallelTestExecution}
import org.scalatest.exceptions.GeneratorDrivenPropertyCheckFailedException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class HelloTests extends AnyFunSuite with Matchers with BeforeAndAfter {


  def over: Boolean = false

  def planet: Boolean = false
  test("displaySalutation returns 'Hello World'") {
    assert(Hello.displaySalutation == "Hello World")

    val war = true
    val over = true

//    war should be over

    val earth = false
//    earth should be a planet

    val voltage = 12.2
    voltage should be (12.0 +- 0.5)


  }

  import org.scalatest.fixture
  import java.io._

  class ExampleSpec extends fixture.FlatSpec with ParallelTestExecution {
    case class FixtureParam(file: File, writer: FileWriter)

    def withFixture(test: OneArgTest) = {
      val file = File.createTempFile("hello", "world") // create the fixture
      val writer = new FileWriter(file)
      val theFixture = FixtureParam(file, writer)
      try {
        writer.write("ScalaTest is ")
        // set up the fixture
        withFixture(test.toNoArgTest(theFixture))
        // "loan" the fixture to the test
      } finally writer.close()
      // clean up the fixture
    }
    "Testing" should "be easy" in { f =>
      f.writer.write("easy!")
      f.writer.flush()
      assert(f.file.length === 18)
    }

    it should "be fun" in { f =>
      f.writer.write("fun!")
      f.writer.flush()
      assert(f.file.length === 17)
    }
  }


}


//import org.scalatest._
//import matchers._
//trait CustomMatchers {
//  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
//    def apply(left: java.io.File) = {
//      val name = left.getName
//      MatchResult (
//        name.endsWith(expectedExtension),
//        s"""File $name did not end with extension "$expectedExtension"""", s"""File $name ended with extension "$expectedExtension""""
//      )
//    }
//  }
//  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
//}
//
//// Make them easy to import with:
//// import CustomMatchers._
//class CustomMatchers extends CustomMatchers with AnyFunSuite with Matchers {
//
//  test("displaySalutation returns 'Hello World'") {
//    // use
//    val file = new File("ok")
//    file should not endWithExtension "txt"
//  }
//
//}
//
