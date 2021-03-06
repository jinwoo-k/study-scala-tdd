package com.packt.examples.getfixture

import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

class ExampleSpec extends FlatSpec {
  def fixture = new {
    val builder = new StringBuilder("ScalaTest is ")
    val buffer = new ListBuffer[String]
  }

  "Testing" should "be easy" in {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    f.buffer += "sweet"
  }

  it should "be fun" in {
    val f = fixture
    f.builder.append("fun!")
    assert (f.builder.toString === "ScalaTest is fun!")
    assert (f.buffer.isEmpty)
  }

  it should "be life" in {
    val f = fixture
    import f._
    builder.append("life!")
    assert (builder.toString === "ScalaTest is life!")
    assert (buffer.isEmpty)
  }
}
