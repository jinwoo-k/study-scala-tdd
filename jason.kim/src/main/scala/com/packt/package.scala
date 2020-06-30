package com

package object packt {
  sealed trait Number {
    def number: String

    require(number forall { c =>
      Character.isDigit(c) || Seq('A', 'B', 'C', 'D', 'E', 'F').contains(c)},
      "Unable to convert string to number")
  }

  case class Binary(number: String) extends Number
  case class Decimal(number: String) extends Number
  case class Hexadecimal(number: String) extends Number
}