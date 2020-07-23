package com.packt.mock

import com.packt.mock.Greetings.Formatter
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class GreetSpec extends AnyFlatSpec with MockFactory {

  "Greetings" should "work as expected." in {
    val mockFormatter = mock[Formatter]

    (mockFormatter.format _)
      .expects("Mr Bond")
      .returning("Ah, Mr Bond. I've been expecting you")
      .once()

    Greetings.sayHello("Mr Bond", mockFormatter)
  }

  "Greetings" should "work as expected when throw exception" in {
    val brokenFormatter = mock[Formatter]

    (brokenFormatter.format _)
      .expects(*)
      .throwing(new NullPointerException)
      .anyNumberOfTimes()

    intercept[NullPointerException] {
      Greetings.sayHello("Erza", brokenFormatter)
    }
  }

  "Greetings" should "work as expected when response dynamically" in {
    val australianFormat = mock[Formatter]

    (australianFormat.format _)
      .expects(*)
      .onCall { s: String => s"G'day $s" }
      .twice()

    Greetings.sayHello("Wendy", australianFormat)
    Greetings.sayHello("Gray", australianFormat)
  }

  "Greetings" should "work as expected when verifying parameters dynamically (two flavours)" in {
    val teamNatsu = Set("Natsu", "Lucy", "Happy", "Erza", "Gray", "Wendy", "Carla")
    val formatter = mock[Formatter]

    def assertTeamNatsu(s: String): Unit = {
      assert(teamNatsu.contains(s))
    }

    // argAssert fails early
    (formatter.format _)
      .expects(argAssert(assertTeamNatsu _))
      .onCall { s: String => s"Yo $s" }
      .once()

    // 'where' verifies at the end of the test
    (formatter.format _)
      .expects(where { s: String => teamNatsu contains (s) })
      .onCall { s: String => s"Yo $s" }
      .twice()

    Greetings.sayHello("Carla", formatter)
    Greetings.sayHello("Happy", formatter)
    Greetings.sayHello("Lucy", formatter)
    Greetings.sayHello("Lucy", formatter)
  }

  "Greetings" should "work as expected when call ordering" in {
    val mockFormatter = mock[Formatter]

    inAnyOrder {
      (mockFormatter.format _)
        .expects("Mr Bond")
        .returns("Ah, Mr Bond. I've been expecting you")
      (mockFormatter.format _)
        .expects("Natsu")
        .returns("Not now Natsu!")
        .atLeastTwice()
    }

    Greetings.sayHello("Natsu", mockFormatter)
    Greetings.sayHello("Natsu", mockFormatter)
    Greetings.sayHello("Mr Bond", mockFormatter)
    Greetings.sayHello("Natsu", mockFormatter)
  }

  "mockFunction" should "work as expected." in {
    //Creates mock function which expects single Int parameters and returns a String
    val m = mockFunction[Int, String]

    //Then we set expectation on this mock that it is called once //with parameter 86 and it returns "Eighty Six"
    m.expects(86).returning("Eighty Six").once
    println(m(86))
  }

}

trait Building {
  def setNumberOfWindows(i: Int): Unit

  def setNumberOfDoors(i: Int): Unit

  def openDoor: Boolean
}

object Greetings {

  trait Formatter {
    def format(s: String): String
  }

  object EnglishFormatter extends Formatter {
    def format(s: String): String = s"Hello $s"
  }

  object GermanFormatter extends Formatter {
    def format(s: String): String = s"Hallo $s"
  }

  object JapaneseFormatter extends Formatter {
    def format(s: String): String = s"こんにちは $s"
  }

  def sayHello(name: String, formatter: Formatter): Unit = {
    println(formatter.format(name))
  }
}