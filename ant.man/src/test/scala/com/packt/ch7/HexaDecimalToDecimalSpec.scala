package com.packt.ch7

import com.packt.ch3.UnitSpec
import com.packt.packt.Decimal
import org.scalatest.prop.TableDrivenPropertyChecks

class HexaDecimalToDecimalSpec extends UnitSpec with TableDrivenPropertyChecks {

  it should "convert decimal to hex" in {
    val validCombos =
      Table(
        ("1243", "4DB"),
        ("11111122", "A98AD2"),
        ("2435255412343", "2370088A677"),
        ("8765432", "85BFF8")
      )
    forAll(validCombos) { (decStr: String, hexStr: String) =>
      var hex = BaseConversion.decimalToHexadecimal(Decimal(decStr))
      hex.number shouldBe hexStr
    }
  }
}