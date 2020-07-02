package com.packt.ch2

import org.scalatest.FlatSpec

class BeanSpec extends FlatSpec {
  "Decimal" should "throw error when initalised with a non numeric string" in {
    try {
      Decimal("XYZ")
      fail    // 여기를 실행 안하고 오류가 나길 원하기 때문
    } catch {
      case e:IllegalArgumentException => assert(e.getMessage == "requirement failed: Unable to convert string to number")
      case _ =>fail
    }
  }

  "Binary" should "throw error when initalised with a non numeric string" in {
    intercept[IllegalArgumentException] {
      Binary("XYZ")
    }
  }
}