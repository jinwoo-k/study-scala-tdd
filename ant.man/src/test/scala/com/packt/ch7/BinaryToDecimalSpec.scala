package com.packt.ch7

import com.packt.ch3.UnitSpec
import com.packt.packt.Binary
import org.scalatest.prop.TableDrivenPropertyChecks

class BinaryToDecimalSpec extends UnitSpec with TableDrivenPropertyChecks {
  it should "convert binary to decimal" in {
    val validCombos =
      Table(
        ("100100111101",  "2365"),
        ("11110001111110111",  "123895"),
        ("100000000000001110000001",  "8389505"),
        ("1011110101011101001101",  "3102541")
      )
    forAll(validCombos) { (binString:String, decString:String) =>
      var decimal = BaseConversion.binaryToDecimal(Binary(binString))
      decimal.number shouldBe decString
    }
  }

  it should "convert binary to decimal error" in {
    val validCombos =
      Table(
        ("binary", "decimal"),
        ("100100111101", "21365"),  // error , Caused by: org.scalatest.exceptions.TestFailedException: "2[]365" was not equal to "2[1]365"
        ("11110001111110111", "123895"),
        ("100000000000001110000001", "838950x5"),
        ("1011110101011101001101", "3102541")
      )
    forAll(validCombos) { (binString:String, decString:String) =>
      var decimal = BaseConversion.binaryToDecimal(Binary(binString))
      decimal.number shouldBe decString
    }
  }
}