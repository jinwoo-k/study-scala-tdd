package com.packt

import org.scalatest.flatspec.AnyFlatSpec

class BinaryDecimalSpec extends AnyFlatSpec {
  "base conversion utility" should "convert a binary number 1100011 into a decimal number 99" in {
    val decimal: Decimal = BaseConversion.BinaryToDecimal(Binary("1100011"))
    assert(decimal.number == "99")
  }

  it should "convert a binary number 11110101 into a decimal number 245" in {
    val decimal: Decimal = BaseConversion.BinaryToDecimal(Binary("11110101"))
    assert(decimal.number == "245")
  }

  it should "convert a binary number 110001000101 into a decimal number 3141" in {
    val decimal: Decimal = BaseConversion.BinaryToDecimal(Binary("110001000101"))
    assert(decimal.number == "3141")
  }
}
