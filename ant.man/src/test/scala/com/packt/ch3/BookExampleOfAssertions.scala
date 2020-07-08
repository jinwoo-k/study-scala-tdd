package com.packt.ch3

import org.scalatest.FunSuite

class BookExampleOfAssertions extends FunSuite  {
  test("assume what happen") {
    assume(1 == 2) // org.scalatest.exceptions.TestCanceledException 하지만 오류는 안남 "Test Canceled"
  }

  test("cancel") {
    cancel("I cancelled it deliberately")
    1/0
    cancel("thie is not running")
  }

  test("clue") {
    assert("Hello".length == 5, "Message")
    assertResult(5, "Message") {"Hello".length}
    withClue("Message") {
      intercept[IllegalArgumentException] {
        throw new IllegalArgumentException()
      }
    }

    withClue("공통 메시지 뿌리기") {
      assert("Hello".length == 5, "Message")
      assertResult(2, "Message") {"Hello".length}
    }
  }
}
