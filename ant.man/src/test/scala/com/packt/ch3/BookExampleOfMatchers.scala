package com.packt.ch3

import org.scalatest._
import Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult}

class BookExampleOfMatchers extends FlatSpec with Matchers{

//  "test1" in {
  val message = "Hello World"
  message should equal("Hello World")
  message must ("Hello Worl") // 왜 동작 안하지..ㅋㅋ

  message should be ("Hello World")
  message shouldBe "Hello World"
  message should equal ("Hello World")
  message should === ("Hello World")
  message shouldEqual "Hello World"

  case class Person(name: String) {}
  class Animal {}

  message shouldBe a [String]
  val person = Person("ant")
  person should not be an [Animal]

  val names = "asdf".toSeq
//  names should be a [Seq[_]]
//  names should be a [Seq[Char]]
//  }

  message should have length 11
//  population should have size 200

  message should startWith ("Hello")
  message should endWith ("rld")
  message should not include ("Batman")

  message should endWith regex ("Wor.d")
  message should fullyMatch regex ("[A-Z a-zs]+")

  "abbccxxx" should startWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
  "xxxabbccxxx" should include regex ("a(b*)(c*)" withGroups ("bb", "cc"))
  "123zyx321" should startWith regex ("(\\d+)" withGroups("123"))
//  "123zyx321" should endWith regex ("(\\d+)" withGroups("321")) // 이건 오류남
  "123zyx321" should endWith regex ("3(\\d+)" withGroups("21")) // 이건 오류 안남
  "xxxabbcc" should endWith regex ("a(b+)(c+)" withGroups ("bb", "cc"))

  val number = 5
  number should be < 7
  number should be <= 7
//  number should be >= 7
//  number should be > 7

  case class War(name:String) {def isOver = name == "war"}
  val war = War("war")
  war shouldBe 'over

  val voltage = 11.5
  voltage should equal (12.0 +- 0.5)
  voltage should be (12.0 +- 0.5)
//  voltage shouldBe 240 +- 10

  None shouldBe empty
  Some(1) should not be empty
  "" shouldBe empty
  new java.util.HashMap[Int, Int] shouldBe empty
  new { def isEmpty = true} shouldBe empty
  Array(1, 2, 3) should not be empty

  class OddMatcher extends BeMatcher[Int] {
    def apply(left: Int) =
      MatchResult(
        left % 2 == 1,
        left.toString + " was even",
        left.toString + " was odd"
      )
  }
  val odd = new OddMatcher
  1 shouldBe odd


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

  val map = Map("two" -> 3, "ouch" -> 2)
  val traversable = List(7)
  map should (contain key ("two") and not contain value (7))
  traversable should (contain (7) or (contain (8) and have size (9)))
  map should (not be (null) and contain key ("ouch"))

  val option = None
  option shouldEqual None
  option shouldBe None
  option should === (None)
  option shouldBe empty
  val option2 = Some("hi")
  option2 shouldEqual Some("hi")
  option2 shouldBe defined


  "val aaaa: String = 1" shouldNot compile
  "val a: String = 1" shouldNot typeCheck
//  val aaaa: String = "1"
}
