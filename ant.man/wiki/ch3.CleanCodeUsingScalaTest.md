# 3장 스칼라테스트를 이용한 클린코드

- Assertions
- Matchers
- Base test classes
- Test fixtures

https://www.scalatest.org/user_guide 예제가 좋은 듯

## Assertions
- 이전장서 배운거 assert, assertResult, intercept
- 일부러 오류 내기 위해 `fail` 사용 (반대는 `succeed`)
- Assumptions: 조건 충족되었을때 테스트 하기 위함 `assume(database.isAvailable())`
  - assume methods throw TestCanceledException whereas the assert methods throw TestFailedException.
- cancel() : TestCancelledException (테스트 실패는 아님)
- withClue 를 사용해서 추가 메세지 줄 수 있음 (intercept 에는 메시지 못주니까 이런식으로..) + 안에 여러 메서드 넣어서 공통 메시지
  ```Scala
  assert("Hello".length == 5, "Message")
  assertResult(5, "Message") {"Hello".length}
  withClue("Message") {
    intercept[IllegalArgumentException] {
    someMethod()
    }
  }
  ```

## Matchers
- must instead of should, "MustMatchers" 를 사용 시..

```scala
message should be ("Hello World")
message shouldBe "Hello World"
message should equal ("Hello World")
message should === ("Hello to World")
message shouldEqual "Hello World"


message shouldBe a [String]
person should not be an [Animal]

names shouldBe a [Seq[_]]

message should startWith ("Hello")
message should endWith ("rld")
message should not include ("Batman")

///// regex 가능
message should endWith regex ("wor.d")
message should fullyMatch regex ("[A-Za-z ]+")
"abbccxxx" should startWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
"xxxabbccxxx" should include regex ("a(b*)(c*)" withGroups ("bb", "cc"))
//  "123zyx321" should endWith regex ("(\\d+)" withGroups("321")) // 이건 오류남
"123zyx321" should endWith regex ("3(\\d+)" withGroups("21")) // 이건 오류 안남

number should be < 7
number should be <= 7
number should be >= 7
number should be > 7

// Boolean can be tested by prepending a symbol (')
// over 혹은 isOver 를 구현하고 있어야 함
case class War(name:String) {def isOver = name == "war"}
val war = War("war")
war shouldBe 'over
```
```scala
voltage should equal (12.0 +- 0.5)
voltage should be (12.0 +- 0.5)
voltage shouldBe 240 +- 10


None shouldBe empty
Some(1) should not be empty
"" shouldBe empty
new java.util.HashMap[Int, Int] shouldBe empty
new { def isEmpty = true} shouldBe empty
Array(1, 2, 3) should not be empty

// BeMatcher 로 나만의 매처 만들기
class OddMatcher extends BeMatcher[Int] {
  def apply(left: Int) =
    MatchResult(
      left % 2 == 1,
      left.toString + " was even",
      left.toString + " was odd"
    )
}
val odd = new OddMatcher
1 shouldBe odd

List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
Some(0) should contain noneOf (7, 8, 9)
import org.scalactic.StringNormalizations._
(Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)

List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)

List(1, 2, 3) shouldBe sorted

val xs = List(1, 2, 3)
import Inspectors._
forAll (xs) { x => x should be < 10 }

all (xs) should be < 10

all (xs) should be > 0
atMost(2, xs) should be >= 4
atLeast(3, xs) should be < 5
between(2, 3, xs) should (be > 1 and be < 5)
exactly (2, xs) should be <= 2
every (xs) should be < 10

val map = Map("two" -> 3, "ouch" -> 2)
val traversable = List(7)
map should (contain key ("two") and not contain value (7))
traversable should (contain (7) or (contain (8) and have size (9)))
map should (not be (null) and contain key ("ouch"))

val option = None
option shouldEqual None
option shouldBe None
option should === (None)
option shouldBe empty
val option2 = Some("hi")
option2 shouldEqual Some("hi")
option2 shouldBe defined

// "have" can check properties
book should have (
  'title ("Programming in Scala"),
  'author (List("Odersky", "Spoon", "Venners")),
  'pubYear (2008)
)
```

## Base test classes
- 사용할 여러 테스트를 묶어서 제공하는게 좋음, 테스트케이스는 그것만 상속해서 사용
- scalatest 에서 코드 중복을 추상화로 없애기 (중복 제거)
  - refactoring using scala
  - override 'withFixture'
  - mixin a 'before-and-after' trait

https://www.scalatest.org/user_guide/sharing_fixtures

- Calling get-fixture method
  - 테스트할 객체를 미리 선언해서 같이 사용 (import 하면 편함)
- Instantiating fixture-context objects
  - trait 안에 변수 만든 후 테스트에서 사용하면 각 테스트 항목에서 instance 만듬 (재사용가능)
  `"Testing" should "be productive" in new Builder {`
- Overriding withFixture(NoArgTest)
  - 테스트 시작이나 종료시 side effect 처리하기 위함
- Calling loan-fixture methods
  - 대출패턴.. DB 같은거 테스트할때 유용 (+ 여러개 섞기 가능)
- Overriding withFixture(OneArgTest)
- Mixing in BeforeAndAfter
  - 이전까지 보던건 테스트 수행중 fixture 사용하는데 test 시작전/후에 사용하는 방법 (이건 테스트중 오류와 무관하게 실행됨)
  - BeforeAndAfterEach has a beforeEach method that will be run before each test (like JUnit's setUp), and an afterEach method that will be run after (like JUnit's tearDown).
  - BeforeAndAfterAll has a beforeAll method that will be run before all tests, and an afterAll method that will be run after all tests
- Composing fixtures by stacking traits
  - fixture 에서 오류가 나도 수행하도록 try 를 넣으면 BeforeAndAfter 와 비슷

## Test fixtures
