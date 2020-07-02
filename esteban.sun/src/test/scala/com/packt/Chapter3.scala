package com.packt

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import CustomMatchers._

class Chapter3 extends FlatSpec with Matchers {
  val message = "Hello World"
  message should be ("Hello World")
  message shouldBe "Hello World"
  message should equal ("Hello World")
  message should === ("Hello World")
  message shouldEqual "Hello World"

  // MustMatchers Mixin 시 사용 가능 (Matchers 와 함께 Mixin 하면 regex 쪽에서 오류남)
//  message must be ("Hello World")
//  message mustBe "Hello World"
//  message must equal ("Hello World")
//  message must === ("Hello World")
//  message mustEqual "Hello World"

  message shouldBe a [String]
  Person() should not be a [Animal]

  val names: Seq[String] = Seq[String]("Bob", "2", "3")
  names shouldBe a [Seq[_]] // should be a 는 안됨

  val obj1: Person = Person()
  val obj2: Person = Person()
  val obj3: Person = obj1
  obj1 should be theSameInstanceAs obj3

  message should have length 11
  val population: Seq[Int] = 1 to 200
  population should have size 200

  message should startWith ("Hello")
  message should endWith ("rld")
  message should not include ("Batman")

  message should endWith regex ("Wor.d")
  message should fullyMatch regex ("[A-Za-z\\s]+")

  "123zyx321" should startWith regex ("([\\d]+)" withGroups("123"))

  val number = Seq(3, 7, 7, 9)
  number.head should be < 7
  number(1) should be <= 7
  number(2) should be >= 7
  number(3) should be > 7

  War() shouldBe 'over
  Earth() should be a 'planet

  val voltage = Seq(12.3, 11.5, 230)
  voltage.head should equal (12.0 +- 0.5)
  voltage(1) should be (12.0 +- 0.5)
  voltage(2).toInt shouldBe (240 +- 10)

  None shouldBe empty
  Some(1) should not be empty
  "" shouldBe empty
  new java.util.HashMap[Int, Int] shouldBe empty
  new { def isEmpty = true } shouldBe empty
  Array(1, 2, 3) should not be empty

  // 잘 안됨
//  message shouldBe lowercase

  names should contain("Bob")

//  (List("Hi", "Di", "Ho") should contain ("ho")) (after being lowerCased)

  List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
  List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
  Some(0) should contain noneOf (7, 8, 9)
//  (Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)

  List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)
  List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
  List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)

  List(1, 2, 3) shouldBe sorted

  val xs = List(1, 2, 3)
//  forAll (xs) { x => x should be < 10 }

  all (xs) should be < 10
  all (xs) should be > 0
  atMost(2, xs) should be >= 4
  atLeast(3, xs) should be < 5
  between(2, 3, xs) should (be > 1 and be < 5)
  exactly (2, xs) should be <= 2
  every (xs) should be < 10

  val map = Map("two" -> 8, "ouch" -> 0)
  map should (contain key ("two") and not contain value (7))
  (1 to 9) should (contain (7) or (contain (8) and have size (9)))
  (8 to 16) should (contain (7) or (contain (8) and have size (9)))
  map should (not be (null) and contain key ("ouch"))

  val option1: Option[String] = None
  option1 shouldEqual None
  option1 shouldBe None
  option1 should === (None)
  option1 shouldBe empty

  val option2: Option[String] = Some("hi")
  option2 shouldEqual Some("hi")
  option2 shouldBe defined

  val book = new Book()
  book should have (
    'title ("Programming in Scala"),
    'author (List("Odersky", "Spoon", "Venners")),
    'pubYear (2008),
    'bestSeller (true)
  )

  val file = new File("~/.delfino.conf")
//  file should not endWithExtension "txt"

  val hidden = 'hidden
  file shouldBe hidden

  "val a: String = 1" shouldNot compile
  "val a: String = 1" shouldNot typeCheck
  "val a: Int = 1" should compile
}

case class Person()
case class Animal()
case class War() { def over: Boolean = true }
case class Earth() { def isPlanet: Boolean = true }
class Book() {
  val title = "Programming in Scala"
  def author = List("Odersky", "Spoon", "Venners")
  def getPubYear: Int = 2008
  def isBestSeller: Boolean = true
}

import org.scalatest._
import matchers._

trait CustomMatchers {
  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
    def apply(left: java.io.File): MatchResult = { val name = left.getName
      MatchResult(
        name.endsWith(expectedExtension),
        s"""File $name did not end with extension "$expectedExtension"""",
        s"""File $name ended with extension "$expectedExtension""""
      )
    }
  }
//
//  class LowerCaseMatcher(targetString: String) extends Matcher[String] {
//    def apply(left: String): MatchResult = {
//      MatchResult(
//        targetString.toLowerCase == targetString,
//        "is not lowercase",
//        "lowercase"
//      )
//    }
//  }

  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
//  def lowercase(string: String) = new LowerCaseMatcher(string)
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers

import org.scalatest._
abstract class UnitSpec
  extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors

