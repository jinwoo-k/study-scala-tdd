package com.packt.ch5

import org.scalatest.{FlatSpec, PrivateMethodTester}

class PrivateMethodTest extends FlatSpec with PrivateMethodTester {
  "A Person" should "transform correctly" in {
    val p1 = PrivateMethodClass(1)
    val transform = PrivateMethod('getPlusAge)
    val transform2 = PrivateMethod[Int]('getPlusAge)
    // We need to prepend the object before invokePrivate to ensure
    // the compiler can find the method with reflection
    val result: Int = p1 invokePrivate transform ()
    assert(result == 2)

    val result2: Int = p1 invokePrivate transform2(2)
    assert(result2 == 3)
  }
}
