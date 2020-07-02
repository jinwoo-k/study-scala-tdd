package com.packt

import org.scalatest.flatspec.AnyFlatSpec

class BinaryDecimalBinarySpec extends AnyFlatSpec {
  "base conversion utility" should "convert a binary number 100100111101 into a decimal number X, and then convert decimal number x into a binary number 100100111101" in {
    val inBinary = Binary("100100111101")
    val outBinary: Binary =  BaseConversion.decimalToBinary(BaseConversion.binaryToDecimal(inBinary))
    assert(inBinary == outBinary)
  }
}
