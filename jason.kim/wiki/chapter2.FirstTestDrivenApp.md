# Chapter2. First Test-Driven Application
이 장에서는 TDD의 기본과 연습문제를 더 자세히 다룬다. 책 전체에서 같은 문제를 만들것이며, 각 장에서 더 많은 요구사항을 추가하고 문제를 더 복잡하게 만들어 갈 것이다.

## Testing frameworks
스칼라의 대표적인 테스팅 프레임웍으로 ScalaTest와 Specs2가 있다. ScalaTest는 Junit과 비슷하고, Specs2는 산문적이지 않고 이상적인 불병성에 더 가깝다.

ScalaTest가 Junit으로 전환이 쉬워 더 보편적으로 사용된다.

## ScalaTest
스칼라테스트는 다양한 스타일의 테스트 구문을 지원하며 BDD, TDD 양쪽 모두 이용 가능하다. 또한 다양한 서드파티 프레임웍(Junit, TestNG, Ant, SBT, ScalaCheck, JMock, EasyMock, ScalaMock, Slenium 등)과 통합 가능하다. 

### A quick tutorial

#### FunSuite
This is good for the transition from xUnit with vivid test names:
```scala
import org.scalatest.FunSuite
class AddSuite extends FunSuite {
	test("3 plus 3 is 6") {
		assert((3 + 3) == 6)
	}
}
```

#### FlatSpec
The structure of this test is flat—like xUnit, but the test name can be written in specification style:
```scala
import org.scalatest.FlatSpec class AddSpec extends FlatSpec {
	"Addition of 3 and 3" should "have result 6" in {
		assert((3 + 3) == 0)
	}
}
```

#### FunSpec
This is more analogous to Ruby's RSpec:
```scala
import org.scalatest.FunSpec class AddSpec extends FunSpec{
	describe("Addition") {
		describe("of 3 and 3") {
			it("should have result 6") {
				assert((3 + 3) == 6)
			}
		}
	}
}
```

#### WordSpec
This has a similar structure to Specs2:
```scala
import org.scalatest.WordSpec class AddSpec extends WordSpec {
	"Addition" when {
		"of 3 and 3" should {
			"have result 6" in {
				assert((3 + 3) == 6)
			}
		}
	}
}
```

#### FreeSpec
This gives outright freedom on specification text and structure:
```scala
import org.scalatest.FreeSpec class AddSpec extends FreeSpec {
	"Addition" - {
		"of 3 and 3" - {
			"should have result 6" in {
				assert((3 + 3) == 6)
			}
		}
	}
}
```


#### Spec
This allows you to define tests as methods:
```scala
import org.scalatest.Spec class AddSpec extends Spec {
	object `Addition` {
		object `of 3 and 3` {
			def `should give result 6` {
				assert((3 + 3) == 6)
			}
		}
	}
}
```

#### PropSpec
This is used for writing tests that are driven through a matrix of data:
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
		forAll(examples) { (a, b, result) =>
			(a + b) should be (result)
		}
	}
}
```

#### FeatureSpec
This is primarily intended for writing BDD style acceptance tests. This allows for the use of ubiquitous language that can be understood by non-programmers:

```scala
import org.scalatest._
class Calculator {
	def add(a:Int, b:Int): Int = a + b
}

class CalcSpec extends FeatureSpec with GivenWhenThen {
	info("As a calculator owner")
	info("I want to be able add two numbers")
	info("so I can get a correct result") feature("Addition") {
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

## Problem statements
이 장에서 정의하는 문제는 책 전체에 걸쳐 사용될 것이다. 다음 장에서도 이 문제를 기반으로 이용 할 것이다. 우리는 기본 변환 API를 만드는 것 이다. 초기에는 십진수를 이진수로 변환할 것 이며, 그 반대의 경우도 다룬다. 이후 변환을 위한 더 많은 기반을 추가한다.
이것을 게르킨어로 적자. Gherkin은 비즈니스 판독 가능 도메인 별 언어로, 해당 동작이 구현되는 방법을 자세히 설명하지 않고 소프트웨어의 동작을 설명할 수 있다. (참고 : https://docs.behat.org/en/v2.5/guides/1.gherkin.html )

Feature : 십진수를 이진수로 변환
사용자로서 십진수 숫자를 이진수 숫자로 변환하고 싶다.

시나리오1 : 
 - Given 숫자 A를 갖는다.
 - When 이 숫자를 이진수로 변환한다.
 - Then, 원래 십진수와 동일한 이진수를 B를 얻는다.

시나리오2 : 
 - Given 이진수 X를 갖는다.
 - When 이 숫자를 십진수로 변환한다.
 - Then, 원래 이진수와 동일한 십진수 Y를 얻는다.

시나리오3 : 
 - Given 십진수 A를 갖는다.
 - When A를 이진수로 변환해 이진수 B를 얻는다.
 - And 다시 이진수 B를 십진수로 C로 변환한다.
 - Then, A는 C와 동일하다.


## Write a failing test – RED
시나리오 1에 대한 테스트 케이스를 생성한다.
```scala

package com.packt
import org.scalatest.FlatSpec

class DecimalBinarySpec extends FlatSpec{
	"base conversion utility" should "convert a number 99 into a binary number 1100011" in {
		var binary:Binary = BaseConversion.decimalToBinary(Decimal("99"))
		assert(binary.number == "1100011")
	}
}
```



