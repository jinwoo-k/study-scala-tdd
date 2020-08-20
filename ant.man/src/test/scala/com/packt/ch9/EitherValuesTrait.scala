package com.packt.ch9

import com.packt.ch7.UnitSpec

class EitherValuesTrait extends UnitSpec {
  // 1
//  val either: Either[String, Int] = Left("Muchas problemas")
//  either.right.get should be > 9 // either.right.get throws NoSuchElementException

//  val either: Either[String, Int] = Left("Muchas problemas")
//  either should be ('right) // throws TestFailedException
//  either.right.get should be > 9

  //3
  val either: Either[String, Int] = Left("Muchas problemas")
  either should be ('left) // throws TestFailedException
  either.left.get should be ("Muchas problema")

}
