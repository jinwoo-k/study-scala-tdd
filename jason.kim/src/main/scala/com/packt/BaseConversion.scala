package com.packt

import scala.annotation.tailrec

object BaseConversion {

  val hexTable = Array('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F')

  @tailrec
  private def toHexadecimal(num: BigInt, acc: String): String = {
    if (num < 16) {
      hexTable(num.toInt).toString + acc
    } else {
      toHexadecimal(num / 16, hexTable((num mod 16).toInt).toString ++ acc)
    }
  }

  def hexadecimalToDecimal(hexadecimal: Hexadecimal): Decimal = {
    val seq = hexadecimal.number.reverse.zipWithIndex.map { case (a, i) =>
      hexTable.indexOf(a)  * math.pow(16, i)
    }
    Decimal(seq.sum.toInt.toString)
  }

  def decimalToHexadecimal(decimal: Decimal): Hexadecimal = {
    Hexadecimal(toHexadecimal(BigInt(decimal.number), ""))
  }

  def decimalToBinary(decimal: Decimal): Binary = {
    Binary(toBinary(BigInt(decimal.number), "").toString)
  }

  @tailrec
  private def toBinary(num: BigInt, acc: String): String = {
    if (num < 2) {
      num.toString + acc
    } else {
      toBinary(num / 2, (num mod 2).toString ++ acc)
    }
  }

  def binaryToDecimal(binary: Binary): Decimal = {
    val seq = binary.number.reverse.zipWithIndex.map { case (a, i) =>
      a.toString.toInt * math.pow(2, i)
    }

    Decimal(seq.sum.toInt.toString)
  }
}
