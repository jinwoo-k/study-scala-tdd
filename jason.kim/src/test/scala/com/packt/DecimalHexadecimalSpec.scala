package com.packt

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DecimalHexadecimalSpec extends AnyFlatSpec with Matchers {
  "base conversion utility" should "convert a number 1243 into a binary number 4DB" in {
    val hex: Hexadecimal = BaseConversion.decimalToHexadecimal(Decimal("1243"))
    assert(hex.number == "4DB")
  }

  it should "convert a number 11111122 into a hexadecimal number A98AD2" in {
    val hex: Hexadecimal = BaseConversion.decimalToHexadecimal(Decimal("11111122"))
    hex.number should be ("A98AD2")
  }
}
