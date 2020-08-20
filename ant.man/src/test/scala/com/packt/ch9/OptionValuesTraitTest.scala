package com.packt.ch9

import com.packt.ch7.UnitSpec

/**
 * https://www.scalatest.org/user_guide/using_OptionValues
 */
class OptionValuesTraitTest extends UnitSpec {

  /*
  val opt: Option[Int] = None

  opt should be ('defined) // throws TestFailedException
  opt.get should be > 9
*/
  val opt: Option[Int] = None

  opt should be ('defined) // throws TestFailedException
  opt.get should be > 9
}
