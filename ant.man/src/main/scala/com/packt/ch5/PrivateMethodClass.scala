package com.packt.ch5

// https://stackoverflow.com/questions/21369093/how-can-a-private-class-method-be-tested-in-scala
// https://www.scalatest.org/user_guide/using_PrivateMethodTester
case class PrivateMethodClass(age: Int) {
  private def getPlusAge(): Int = age + 1

  private def getPlusAge(plusAge:Int): Int = plusAge + age
}