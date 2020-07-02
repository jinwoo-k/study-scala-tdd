package com.packt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class HelloTests extends AnyFunSuite with Matchers {


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


}
