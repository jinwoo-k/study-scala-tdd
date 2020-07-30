package com.packt.ch7

import com.packt.ch3.UnitSpec
import com.packt.packt.Decimal
import org.scalatest.prop.TableDrivenPropertyChecks

class DecimalBinarySpec extends UnitSpec with TableDrivenPropertyChecks {

  it should "convert decimal to binary" in {
    val validCombos =
      Table(
        ("100100111101", "2365"),
        ("11110001111110111", "123895"),
        ("100000000000001110000001", "8389505"),
        ("1011110101011101001101", "3102541")
      )
    forAll(validCombos) { (binString: String, decString: String) =>
      var binary = BaseConversion.decimalToBinary(Decimal(decString))
      binary.number shouldBe binString
    }
  }
}