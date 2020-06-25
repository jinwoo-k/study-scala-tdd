# 2장 첫 test driven application
이장 내용 : Testing frameworks, ScalaTest, Problem statement, IDE, Implementation

## Testing frameworks
- two main frameworks
    - ScalaTest: uses the JUnit like testing structure + ScalaTest는 JUnit에서 쉽게 전환 할 수 있기 때문에 더 인기
    - Specs2: 산문은 아니고 불변적 생각에 가깝다
    
일단 ScalaTest 로 시작 후 이 후 장에서 둘다 비교

## ScalaTest
- 젤 유명하고 BDD and TDD 둘다 가능, (이후 장에서 BDD 로 설명 예정)
    - party frameworks such as JUnit, TestNG, Ant, Maven, SBT, ScalaCheck, JMock, EasyMock, Mockito, ScalaMock, Selenium, and so on
    - integrates with IDEs like Eclipse, NetBeans, and IntelliJ.

## A quick tutorial

- FunSuite
    - This is good for the transition from xUnit with vivid test names:
```scala
import org.scalatest.FunSuite
  class AddSuite extends FunSuite {
    test("3 plus 3 is 6") {
    assert((3 + 3) == 6)
  }
}
```

- FlatSpec
    - The structure of this test is flat—like xUnit, but the test name can be written in specification style
```scala
import org.scalatest.FlatSpec
class AddSpec extends FlatSpec {
  "Addition of 3 and 3" should "have result 6" in {
  assert((3 + 3) == 0)
  }
}
```
- FunSpec
    - This is more analogous to Ruby's RSpec
```scala
import org.scalatest.FunSpec
class AddSpec extends FunSpec{
  describe("Addition") {
    describe("of 3 and 3") {
      it("should have result 6") {
        assert((3 + 3) == 6)
      }
    }
  }
}
```

- WordSpec
    - This has a similar structure to Specs2:
```scala
import org.scalatest.WordSpec
class AddSpec extends WordSpec {
  "Addition" when {
    "of 3 and 3" should {
      "have result 6" in {
        assert((3 + 3) == 6)
      }
    }
  }
}
```
- FreeSpec
    - This gives outright freedom on specification text and structure:
```scala
import org.scalatest.FreeSpec
class AddSpec extends FreeSpec {
  "Addition" - {
    "of 3 and 3" - {
      "should have result 6" in {
        assert((3 + 3) == 6)
      }
     }
   }
}
```
- Spec
    - This allows you to define tests as methods:
```scala
import org.scalatest.Spec
class AddSpec extends Spec {
  object `Addition` {
    object `of 3 and 3` {
      def `should give result 6` {
        assert((3 + 3) == 6)
      }
    }
  }
}
```
- PropSpec
    - This is used for writing tests that are driven through a matrix of data:
```scala
import org.scalatest._
import prop._
class AddSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {
  val examples =
    Table(
      ("a", "b", "result"),
      (3, 3, 6),
      (4, 5, 9)
    )
    property("Addition of two numbers") {
      forAll(examples) {
        (a, b, result) =>
        (a + b) should be (result)
      }
    }
}
```
- FeatureSpec
    - This is primarily intended for writing BDD style acceptance tests. This allows for the use of ubiquitous language that can be understood by non-programmers:
```scala
import org.scalatest._
class Calculator {
  def add(a:Int, b:Int): Int = a + b
}
class CalcSpec extends FeatureSpec with GivenWhenThen {
  info("As a calculator owner")
  info("I want to be able add two numbers")
  info("so I can get a correct result")
  feature("Addition") {
    scenario("User adds two numbers") {
      Given("a calculator")
      val calc = new Calculator
      When("two numbers are added")
      var result = calc.add(3, 3)
      Then("we get correct result")
      assert(result == 6)
    }
  }
}
```

- Most of the book's examples will be built using FlatSpec and FeatureSpec.


## problem statement (코드 확인)
- Feature: decimal to binary conversion:
    - As a user, I want to convert a decimal number to a binary number:
- Scenario 1:
    - Given I have a number A
    - When I convert this number to a binary number
    - Then, I get a binary equivalent B of the original decimal number
- Scenario 2:
    - Given I have binary number X
    - When I convert this number to a decimal number
    - Then, I get a decimal equivalent Y of the original binary number
- Scenario 3:
    - Given I have decimal number A
    - When I convert A to binary to get binary number B
    - And again convert B to decimal number C
    - Then A is equal to C
