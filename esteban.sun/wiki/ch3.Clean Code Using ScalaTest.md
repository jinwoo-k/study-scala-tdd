# Chapter3. Clean Code Using ScalaTest
이 장에서는 ScalaTest 를 사용하여 클린코드를 작성하기 위한(코드를 정리하는) 몇가지 예제를 살펴본다.
* Assertion
* Matchers
* Base test classes
* Test fixture
* 참조 : https://www.scalatest.org/user_guide/using_matchers

## * Assertion
ScalaTest 와 함께 제공되는 기본적인 Assertion 은 3가지가 존재함<br>
FunSuite 형식으로 설명

* assert (제공된 체크 로직이 true 여야만 통과)
    ```
    test("one plus one") {
        assert(1 + 1 == two)
        assert(1 + 1 != three)
    }
    ```

* assertResult (결과값을 설정하고 메소드를 정의하는 형식으로 같아야 통과)
    ```
    test("one plus one with result") {
        val two = 2
        assertResult(two) { 1 + 1 }
    }
    ``` 
  
* intercept (Exception 발생 여부를 체크하여 주어진 Exception 발생시 통과)
    ```
    intercept[IllegalArgumentException] {
        someMethod()
    }
    
    # JUnit3 형식의 아래와 동일함
    try {
        someMethod()
        fail("Shouldn't be here") // 테스트 실패
    } catch {
        case _: IllegalArgumentException => // Expected so continue 
        case _ => fail("Unexpected exception thrown") // 테스트 실패
    }
    ```
    
## Deliberately failing tests (의도적 실패 테스트)
```
fail()
fail("Failure message")
```
위의 구문은 TestFailException을 발생시킨다.

## Assumptions (가정)
기본 Assertion 외에도 Assertion 내에서 특별한 기능을 사용할 수 있다. 이 특성은 일부 가정이 충족되지 않으면 테스트를 취소 할 수있는 방법을 제공하며, 이 기능에 의해 취소가 되더라도 테스트는 실패로 처리되지 않는다. 이는 테스트가 작동하기 위해 필요한 일부 전제 조건을 확인데 사용할 수 있다.
TestCanceledException 을 발생시키며, false 시 뒤의 message 를 리턴함 ("Duh!!")
```
assume(database.isAvailable(), "Duh!!")
```

## Canceling tests
cancle() 메서드는 TestCancelException을 발생시켜 의도적으로 테스트를 취소한다. cancel(message) 형태로도 사용 가능

## Failure messages and clues
assert, assertResult는 실패 단서에 대한 부분을 message 파라미터로 줄 수 있으나 intercept는 불가능한데 이는 withClue 를 통해 동일한 효과를 줄 수 있다. <br>
The withClue method will only modify the detailed message of an exception that is mixin the ModifiableMessage trait.
```
assert("Hello".length == 5, "Message")

assertResult(5, "Message") {"Hello".length}

withClue("Message") {
    intercept[IllegalArgumentException] {
        someMethod()
    }
}
```

## * Matchers
* ScalaTest는 Assertion 뿐 아니라 DSL(Domain-Specific Language) 도 지원함
* should 라는 단어를 사용하며, Matchers trait 을 추가하여 사용할 수 있다.
* FlatSpec 과 함께 사용
* 조건을 만족하지 못하는 경우 TestFailedException 을 발생함
* MustMatchers 는 should 대신 must 를 사용 (동일한 용법)
```
import org.scalatest._
class PacktSpec extends FlatSpec with Matchers

message should equal ("Hello World")
```

### Matchers 의 다양한 용법 

#### Matchers for equality
```
message should equal ("Hello World")
message should be ("Hello World")
message shouldBe "Hello World"
message should === ("Hello to World")
message shouldEqual "Hello World"
```

#### Matchers for instance and identity checks of objects
```
# Instance Type Check
message shouldBe a [String]
person should not be an [Animal]

names should be a [Seq[_]] ??

# Identity Check
obj1 should be theSameInstanceAs obj2
```

#### Matchers for size and length
```
# 어떠한 타입 T 라도 implicit Length[T] 가 가능하면 사용 가능
# size 또한 마찬가지로 implicit Size[T] 가 가능하면 사용 가능

message should have length 10
population should have size 200
```

#### Matching strings
```
# 일반 String Matching
message should startWith ("Hello")
message should endWith ("rld")
message should not include ("Batman")

# regex 사용한 Matching
message should endWith regex ("wor.d") -> regex 추가 필요
message should fullyMatch regex ("[A-Za-zs]+") -> s 대신 공백 또는 \\s 로 변경

# regex 와 requirement 조합 (regex 에 해당하는 값과 123 이 같아야만 통과)
"123zyx321" should startWith regex ("([\\d]+)" withGroups("123")) -> d 대신 \\d 로 변경
```

#### Matching greater and less than
```
# 어떠한 T 타입이라도 implicit Ordering[T] 를 구현하고 있으면 사용 가능 
number should be < 7
number should be <= 7
number should be >= 7
number should be > 7
```

#### Matching Boolean properties
```
# war 객체가 over 또는 isOver 를 구현하고 있어야 함
# earth 객체가 planet 또는 isPlanet 을 구현하고 있어야 함
# 해당 함수가 false 인 경우 TestFailedException 가 리턴되며 책과는 좀 다르게 리턴됨

war shouldBe 'over  -> War() was not over
earth should be a 'planet  -> Earth() was not a planet

# Range 를 가지는 number 의 경우 +- Operator 사용 가능
# Type 주의 필요 (Int, Double)
voltage should equal (12.0 +- 0.5)
voltage should be (12.0 +- 0.5)
voltage shouldBe 240 +- 10
```

#### Matching emptiness
```
# Option, String Length, Collection Size, Function(isEmpty) 에 다양하게 사용 가능 
None shouldBe empty
Some(1) should not be empty
"" shouldBe empty
new java.util.HashMap[Int, Int] shouldBe empty
new { def isEmpty = true} shouldBe empty
Array(1, 2, 3) should not be empty
```

#### Writing your own BeMatchers
사용자 정의 BeMatcher를 만들 수 있다. (뒤에 Custom Matcher 참고)
```
message shouldBe lowercase
```

### Some more Matchers
#### Matchers for containers
```
# 어떠한 T 타입이라도 implicit org.scalatest.enabler.Containing[T] 를 구현하고 있으면 사용 가능
# GenTraversable, Collection, Map, String, Array, Option 등

listOfNames should contain("Bob")

# oneOf, noneOf, alLeastOneOf ...
List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
Some(0) should contain noneOf (7, 8, 9)
(Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)

List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3) -> inOrder + Only (다른 값 존재하면 안됨)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)

# sortable object 에서 사용 가능
List(1, 2, 3) shouldBe sorted

val xs = List(1, 2, 3)
forAll (xs) { x => x should be < 10 } -> forAll 오류 ?

# 더 나이스 한 표현
all (xs) should be < 10
```
* all: 모든 요소들이 조건을 만족하면 성공
* atLeast: 조건을 만족하는 개수가 적어도 지정된 값을 넘으면 성공 
* atMost: 조건을 만족하는 개수가 많아도 지정된 값을 넘지 않으면 성공 
* between: 조건을 만족하는 개수가 최소/최대 값 사이에 존재하면 성공 
* every: all 과 동일하나 실패시 모든 실패 결과를 나열함 (나머지는 모두 처음 실패 케이스만 나열)
* exactly: 조건을 만조하는 개수가 지정된 값과 일치하면 성공

```
all (xs) should be > 0
atMost(2, xs) should be >= 4
atLeast(3, xs) should be < 5
between(2, 3, xs) should (be > 1 and be < 5)
exactly (2, xs) should be <= 2
every (xs) should be < 10
```

#### Combining Matchers with logical expressions
```
# and, or, not 을 조합한 Matchers

map should (contain key ("two") and not contain value (7))
(1 to 9) should (contain (7) or (contain (8) and have size (9))) <- 예제는 traversable 로 되어 있음
(8 to 16) should (contain (7) or (contain (8) and have size (9))) <- 예제는 traversable 로 되어 있음
map should (not be (null) and contain key ("ouch"))
```

#### Matching options
```
# Option 이 None 일때

option shouldEqual None
option shouldBe None
option should === (None)
option shouldBe empty

# Option 이 값을 가지고 있을때

option shouldEqual Some("hi")
option shouldBe defined
```

#### Matching properties
have를 이용해 객체의 퍼블릭 필드, 메서드, JavaBean-Style 의 get/is 메서드 속성을 검사할 수 있다.
```
book should have (
  'title ("Programming in Scala"),
  'author (List("Odersky", "Spoon", "Venners")),
  'pubYear (2008),
  'bestSeller (true)
)
```

#### Custom Matchers (create own Matchers, call endWithExtension)
```scala
import org.scalatest._
import matchers._

trait CustomMatchers {
  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
    def apply(left: java.io.File): MatchResult = { val name = left.getName
      MatchResult(
        name.endsWith(expectedExtension),
        s"""File $name did not end with extension "$expectedExtension"""",
        s"""File $name ended with extension "$expectedExtension""""
      )
    }
  }

  class LowerCaseMatcher(targetString: String) extends Matcher[String] {
    def apply(left: String): MatchResult = {
      MatchResult(
        targetString.toLowerCase == targetString,
        "is not lowercase",
        "lowercase"
      )
    }
  }

  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
  def lowercase(string: String) = new LowerCaseMatcher(string)
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers
```

#### Checking that a snippet of code does not compile
```
"val a: String = 1" shouldNot compile
"val a: String = 1" shouldNot typeCheck
"val a: Int = 1" should compile
```


## * Base test classes
스칼라테스트는 유사하거나 고유한 문제에 대한 솔루션에 중점을 둔 많은 경량 trait 으로 구성되어 문제 해결을 위해 다양하게 Mixin 하여 활용한다.
이런 경우를 위해 많은 경량 trait 이 Mixin 된 abstract class 를 정의하여 이를 활용하도록 하면 프로젝트의 모든 테스트에서 균일한 DSL 을 제공할 수 있으며,
이는 중복제거 및 빠른 컴파일을 가능하게 함
```scala
package com.packt
import org.scalatest._

abstract class UnitSpec extends FlatSpec
                           with Matchers
                           with OptionValues
                           with Inside
                           with Inspectors
                           
class DecimalBinarySpec extends UnitSpec {...}
class BeanSpec extends UnitSpec (---)
class BinaryToDecimalSpec extends UnitSpec {...}
``` 
대부분의 프로젝트들은 다양한 베이스 테스트클래스들을 갖기도 한다. (DbSpec, ActorSysSpec, DbActorSysSpec 등)

## * Test fixtures
Test fixture 는 클래스, 기타 라이브러리 및 아티팩트의 모음이고, 테스트에 필요한 파일, 소켓, 데이터베이스 연결 등이 될 수 있다. 
Clean Code 원칙에 따라 테스트에서 이러한 픽스쳐를 재사용하고 가능한 한 추상적으로 만들어야 한다.
예를 들어, 두 가지 테스트에서 데이터베이스와 통신해야하는 경우 두 데이터베이스에 동일한 데이터베이스 연결 코드를 재사용해야하는데, 
테스트에서 코드 중복이 많을수록 테스트에서 실제 리팩토링에 대한 부담이 더 커지게 된다.

ScalaTest 에서는 아래의 기술들을 권장하고 있음
* Reafactor using Scala
* Override 'withFixture'
* Mixin a 'before-and-after' trait

이런 기술로 부터 테스트간 종속성을 줄이고, 코드 중복을 줄여 더 쉽게 추론할 수 있고 병렬로 테스트 하는데도 문제 없도록 할 수 있다. 

각각의 권장 사용법 및 기술에 대한 설명
* Refactor using Scala when different tests need different fixtures
  * get-fixture methods
  * fixture-content objects
  * load-fixture methods
  
* Override withFixture when most or all tests need the same fixture
  * withFixture(No ArgsTest)

* mixin a before-and-after trait when you want an aborted suite, not a failed test, if the fixture code fails
  * BeforeAndAfter
  * BeforeAndAfterEach
  
### Calling get-fixture methods
* get-fixture 메소드는 하나 이상의 테스트에 동일한 가변 fixture 객체가 생성되어야 하고 fixture를 사용한 후 정리할 필요가 없을 때 사용
* fixture가 필요한 각 테스트 시작시 리턴 된 오브젝트를 로컬 변수에 저장하여 get-fixture 메소드를 호출 할 수 있음
* get-fixture 메소드는 호출 될 때마다 필요한 fixture 객체 (또는 여러 fixture 객체를 포함하는 holder 객체)의 새 인스턴스를 반환
* 가변 fixture 가 필요한 경우 매개변수를 전달해서 다른 fixture 를 구성할 수 있다.
```scala
package com.packt.examples.getfixture

import org.scalatest.FlatSpec
import collection.mutable.ListBuffer

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
``` 

### Instantiating fixture-context objects
* 테스트 마다 다른 fixture 가 필요한 경우 또는 조합이 필요한 경우 유용한 방법
* fixture 객체를 fixture-context object 의 인스턴스 변수로 정의하는 것이며, 인스턴스화는 테스트 본문을 형성함
* get-fixture 기법과 마찬가지로 사용 후 Clean Up 이 필요 없는 경우에 사용
* 이 기법을 사용하기 위해 Trait 또는 Class 에서 fixture 객체로 초기화 된 인스턴스 변수를 정의한 후
* 각 테스트에서 테스트에 필요한 fixture 오브젝트 만 포함된 오브젝트를 인스턴스화 하여 사용
* Trait 을 사용하면 각 테스트에 필요한 픽스쳐 개체 만 함께 Mixin 할 수 있지만 클래스를 사용하면 생성자를 통해 데이터를 전달하여 픽스쳐 개체를 구성 해야 함 
* 아래 예는 fixture 오브젝트가 두 Trait 으로 분할되는 예이며 각 테스트는 필요한 트레잇을 함께 Mixin 하여 사용
```scala
package com.packt.examples.fixturecontext

import collection.mutable.ListBuffer
import org.scalatest.FlatSpec
 
class ExampleSpec extends FlatSpec {
  trait Builder {
    val builder = new StringBuilder("ScalaTest is ")
  }
  
  trait Buffer {
    val buffer = ListBuffer("ScalaTest", "is")
  }

  // This test needs the StringBuilder fixture
  "Testing" should "be productive" in new Builder {
    builder.append("productive!")
    assert(builder.toString === "ScalaTest is productive!")
  }

  // This test needs the ListBuffer[String] fixture
  "Test code" should "be readable" in new Buffer {
    buffer += ("readable!")
    assert(buffer === List("ScalaTest", "is", "readable!"))
  }

  // This test needs both the StringBuilder and ListBuffer
  it should "be clear and concise" in new Builder with Buffer {
    builder.append("clear!")
    buffer += ("concise!")
    assert(builder.toString === "ScalaTest is clear!")
    assert(buffer === List("ScalaTest", "is", "concise!"))
  }
}
```

### Overriding withFixture(NoArgTest)
* get-fixture 방법과 fixture-context 객체 접근 방식은 각 테스트가 시작될 때 fixture를 설정하여 사용하지만 뒤처리는 하지 않음
* 테스트 시작 또는 종료시 사이드 이펙트를 수행해야하고, 실제로 fixture 오브젝트를 테스트에 전달할 필요가 없는 경우
* override withFixture 를 통해 처리
```scala
// Default implementation in trait Suite
protected def withFixture(test: NoArgTest) = {
  test()
}
```

```scala
// Your implementation
override def withFixture(test: NoArgTest) = {
  // Perform setup
  try super.withFixture(test) // Invoke the test function
  finally {
    // Perform cleanup
  }
}
```

* 아래 예를 기준으로 FlatSpec 은 FlatSpecLike 를 Mixin 했고, FlatSpecLike 는 여러 Trait 을 Mixin 했는데 이중 Suite 에 withFixture 함수가 존재함
* 실패했을때 현재 디렉토리의 파일들을 표출함
```scala
package com.packt.examples.noargtest

import java.io.File
import org.scalatest.{Failed, FlatSpec}

class ExampleSpec extends FlatSpec {
  override def withFixture(test: NoArgTest) = {
    super.withFixture(test) match {
      case failed: Failed =>
        val currDir = new File(".")
        val fileNames = currDir.list()
        info("Dir snapshot: " + fileNames.mkString(", "))
        failed
      case other => other
    }
  }

  "This test" should "succeed" in {
    assert(1 + 1 === 2)
  }

  it should "fail" in {
    assert(1 + 1 === 3)
  }
}
```

### Calling loan-fixture methods
* 테스트코드를 매개변수로 가지는 fixture 함수를 빌려와서 활용하는 기법을 calling loan-fixture methods 라 함
* method 에서 fixture 에 대한 전처리, 후처리를 처리하고 test code 를 fixture 에 매개변수로 전달
* fixture 에 side effect 가 있는... (파일 또는 데이터베이스의 생성/입력 등) 경우 각각의 동작이 독립적으로 수행되도록 처리하면 병렬 동작도 가능
* 아래 예제는 두 개의 fixture (데이터베이스와 파일)를 사용하는 세 가지 테스트 임
둘 다 이후 정리가 필요하므로 각각 론 고정 방법을 통해 제공됩니다.
```scala
package com.packt.examples.loanfixture

import org.scalatest.FlatSpec
import java.util.UUID.randomUUID
import java.io._
import DbServer._

class ExampleSpec extends FlatSpec {
  def withDatabase(testCode: Db => Any) {
    val dbName = randomUUID.toString
    val db = createDb(dbName)

    // create the fixture
    try {
      db.append("ScalaTest is ")
      // perform setup
      testCode(db)
      // "loan" the fixture to the test
    }
    finally removeDb(dbName)
    // clean up the fixture
  }

  def withFile(testCode: (File, FileWriter) => Any) {
    val file = File.createTempFile("hello", "world")

    // create the fixture
    val writer = new FileWriter(file)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      testCode(file, writer)
      // "loan" the fixture to the test
    }
    finally writer.close()
    // clean up the fixture
  }

  // This test needs the file fixture
  "Testing" should "be productive" in withFile {
    (file, writer) =>
      writer.write("productive!")
      writer.flush()
      assert(file.length === 24)
  }

  // This test needs the database fixture
  "Test code" should "be readable" in withDatabase {
    db => db.append("readable!")
    assert(db.toString === "ScalaTest is readable!")
  }

  // This test needs both the file and the database
  it should "be clear and concise" in withDatabase {
    db => withFile { (file, writer) =>
      // loan-fixture methods compose
      db.append("clear!")
      writer.write("concise!")
      writer.flush()
      assert(db.toString === "ScalaTest is clear!")
      assert(file.length === 21)
    }
  }
}

import java.util.concurrent.ConcurrentHashMap

object DbServer {
  // Simulating a database server
  type Db = StringBuffer
  private val databases = new ConcurrentHashMap[String, Db]
  def createDb(name: String): Db = {
    val db = new StringBuffer
    databases.put(name, db)
    db
  }

  def removeDb(name: String) { databases.remove(name) }
}
```

### Overriding withFixture(OneArgTest)
* 모든 또는 대부분의 테스트에 동일한 fixture 가 필요한 경우 fixture.FlatSpec을 상속받아 withFixture(OneArgTest)로 재정의하여 사용하는 것으로 loan-fixture method 의 boilerplate 를 피할 수 있다.
* Fixture.FlatSpec의 각 테스트는 fixture 를 매개 변수로 사용하여 테스트에 전달할 수 있음
* FixtureParam을 지정하여 fixture 매개 변수의 유형을 표시하고 OneArgTest를 사용하는 withFixture 메소드로 구현해야합니다.  
* 테스트 함수를 test(theFixture) 처럼 직접 호출하는 대신 withFixture(NoArgTest) 를 사용하여 테스트 함수를 호출하도록하는 것이 좋다.
* 이렇게하려면 OneArgTest를 NoArgTest로 변환해야 하는데 이는 fixture 오브젝트를 OneArgTest 의 toNoArgTest 메소드에 전달하여이를 수행 가능
* 이 예제에서 테스트에는 File과 FileWriter라는 두 개의 fixture 객체가 필요 -> 이러한 상황에서 FixtureParam 유형을 오브젝트를 포함하는 튜플로 정의하거나 이 예제에서 수행된 것처럼 오브젝트를 포함하는 케이스클래스로 정의할 수 있다.
```scala
package com.packt.examples.oneargtest

import java.io.{File, FileWriter}
import org.scalatest.fixture

class ExampleSpec extends fixture.FlatSpec {

  case class FixtureParam(file: File, writer: FileWriter)

  def withFixture(test: OneArgTest) = {
    val file = File.createTempFile("hello", "world") // create the fixture
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      withFixture(test.toNoArgTest(theFixture))
      // "loan" the fixture to the test
    } finally writer.close()
    // clean up the fixture
  }

  "Testing" should "be easy" in { f =>
    f.writer.write("easy!")
    f.writer.flush()
    assert(f.file.length === 18)
  }

  it should "be fun" in { f =>
    f.writer.write("fun!")
    f.writer.flush()
    assert(f.file.length === 17)
  }
```

### Mixing in BeforeAndAfter
* 지금까지 확인한 방법은 모두 테스트 과정에서 진행되는 일이라 정비과정에서의 실패 또한 테스트 실패로 이어짐
* 이와는 별개로 정리중에 발생하는 이슈와 테스트중 발생하는 이슈를 구분하는 방법으로 활용됨
* 각 테스트의 실행 이전, 실행 이후에 수행돼야 하는 일들을 정의
* 각 테스트 코드간 통신은 인스턴스 변수를 재할당하거나 인스턴스에 val 로 mutable 객체를 생성하는 것처럼 사이드 이펙트를 이용함
* 따라서, 테스트를 병령로 실행할 수 없다.
* 이를 해결하기 위해서 ScalaTest 의 ParallelTestExecution Trait 은 테스트 클래스당 하나의 인스턴스를 할당하여 동작함
* BeforeAndAfter는 테스트 전후에 코드를 실행하는 최소한의 방법을 제공하지만 실행 순서가 명확하지 않기 때문에 스택 가능한 특성을 사용하도록 설계되지 않음
```scala
package com.packt.examples.beforeandafter

import org.scalatest.{BeforeAndAfter, FlatSpec}
import collection.mutable.ListBuffer

class ExampleSpec extends FlatSpec with BeforeAndAfter {
  val builder = new StringBuilder
  val buffer = new ListBuffer[String]

  before {
    builder.append("ScalaTest is ")
  }

  after {
    builder.clear()
    buffer.clear()
  }

  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
  }
}
```
### Composing fixtures by stacking traits
* 대규모 프로젝트에서 팀은 종종 테스트 클래스가 여러 fixture 의 다른 조합 또는 다른 순서로 초기화 (및 정리) 해야 하는 상황이 존재
* ScalaTest 에서 이를 수행하는 좋은 방법은 개별 fixture 를 스택이 가능한 Trait 패턴을 사용하여 구성할 수있는 Trait 으로 인수 분해하는 것
* 예를 들어, 여러 가지 특성에 withFixture 메소드를 배치하면 각각 super.withFixture를 호출하여 수행
* 이전 예제에서 사용 된 StringBuilder 및 ListBuffer[String] fixture 가 Builder 및 Buffer라는 두 개의 스택 가능 fixture Trait 으로 분해 된 예는 다음과 같다.
```scala
package com.packt.examples.composingwithfixture

import org.scalatest._
import collection.mutable.ListBuffer

trait Builder extends SuiteMixin {
  this: Suite =>
  val builder = new StringBuilder

  abstract override def withFixture(test: NoArgTest) = {
    builder.append("ScalaTest is ")
    try super.withFixture(test)
    // To be stackable, must call super.withFixture
    finally builder.clear()
  }
}

trait Buffer extends SuiteMixin {
  this: Suite =>
  val buffer = new ListBuffer[String]
  
  abstract override def withFixture(test: NoArgTest) = {
    try super.withFixture(test)
    // To be stackable, must call super.withFixture
    finally buffer.clear()
  }
}

class ExampleSpec extends FlatSpec with Builder with Buffer {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }
  
  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}
```
위의 ExampleSuite는 Builder와 Buffer Trait 을 혼합하여 두 fixture 를 가져오는데 각 fixture 는 각 테스트 전에 초기화되고 이후에 정리됨 <br>
특성이 혼합 된 순서에 따라 실행 순서가 결정되며, 이 경우 Builder 는 Buffer 에 super 이고, Buffer 가 Builder에 super 가되도록하려면 아래와 같이 혼합 순서를 전환하면 됨
```
class Example2Suite extends Suite with Buffer with Builder

# 한개만 Mixin 하는 경우
class Example2Suite extends Suite with Builder
```
* 쌓을 수있는 fixture Trait 을 만드는 또 다른 방법은 BeforeAndAfterEach 또는 BeforeAndAfterAll 특성을 확장하는 것
* BeforeAndAfterEach에는 각 테스트 전에 실행되는 beforeEach 메소드 (JUnit의 SetUp)와 이후에 실행될 AfterEach 메소드 (JUnit의 tearDown) 가 있다.
* 마찬가지로 BeforeAndAfterAll에는 모든 테스트 전에 실행될 beforeAll 메소드와 모든 테스트 후에 실행될 afterAll 메소드가 있다.
* 아래는 이전에 표시된 예제의 Fixture 대신 BeforeAndAfterEach 메서드를 사용하도록 다시 작성된 경우의 모습
```scala
package com.packt.examples.composingbeforeandaftereach

import org.scalatest._
import collection.mutable.ListBuffer

trait Builder extends BeforeAndAfterEach {
  this: Suite =>
  val builder = new StringBuilder

  override def beforeEach() {
    builder.append("ScalaTest is ")
    super.beforeEach()
    // To be stackable, must call super.beforeEach
  }

  override def afterEach() {
    try super.afterEach()
    // To be stackable, must call super.afterEach
    finally builder.clear()
  }
}

trait Buffer extends BeforeAndAfterEach {
  this: Suite =>
  val buffer = new ListBuffer[String]

  override def afterEach() {
    try super.afterEach()
    // To be stackable, must call super.afterEach
    finally buffer.clear()
  }
}

class ExampleSpec extends FlatSpec with Builder with Buffer {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}
```
* withFixture와 동일한 순서를 얻으려면 앞의 예제와 같이 각 beforeEach 메서드의 끝에 super.beforeEach 호출을, 각 afterEach 메서드의 시작 부분에 super.afterEach 호출을 배치
* try 블록에서 super.afterEach를 호출하고 이전 예제와 같이 finally 에서 Clean Up 을 수행하는 것이 좋음
* afterEach 에서 try 가 사용된 이유는 super.afterEach가 예외를 throw 하더라도 Clean Up 코드가 수행되도록 하기위해서 이다.
* BeforeAndAfterEach를 확장하는 스태킹 Trait 과 Fixture로 구현하는 특성의 차이점
  * BeforeAndAfterEach에서 각각의 테스트 전후에 SetUp 및 CleanUp 이 발생하지만
  * withFixture 에서는 테스트 시작 및 종료시 SetUp 및 CleanUp 발생
  * 따라서 withFixture 메소드가 예외와 함께 갑자기 완료되면 실패한 테스트로 간주되지만, BeforeAndAfterEach의 beforeEach 또는 afterEach 메소드 중 하나가 갑자기 완료되면 실패가 아닌 중단된것으로 간주되어 SuiteAborted 이벤트가 발생

## Problem statement
* 이전 장의 기본 변환에 대한 예제를 10 진수에서 16 진수로 또는 그 반대로 변환 할 수 있도록 확장
* 시나리오 1 : 주어진 10진수 A 를 16진수로 변경하여 계산된 16진수값과 같은지 체크
* 시나리오 2 : 주어진 16진수 X 를 10진수로 변경하여 계산된 10진수값과 같은지 체크
* 시나리오 3 : 주어진 10진수 A 를 16진수로 변환 후 다시 10진수로 변환했을때 최초 10진수 A 와 같은지 체크
# Chapter3. Clean Code Using ScalaTest
이 장에서는 ScalaTest 를 사용하여 클린코드를 작성하기 위한(코드를 정리하는) 몇가지 예제를 살펴본다.
* Assertion
* Matchers
* Base test classes
* Test fixture
* 참조 : https://www.scalatest.org/user_guide/using_matchers

## * Assertion
ScalaTest 와 함께 제공되는 기본적인 Assertion 은 3가지가 존재함<br>
FunSuite 형식으로 설명

* assert (제공된 체크 로직이 true 여야만 통과)
    ```
    test("one plus one") {
        assert(1 + 1 == two)
        assert(1 + 1 != three)
    }
    ```

* assertResult (결과값을 설정하고 메소드를 정의하는 형식으로 같아야 통과)
    ```
    test("one plus one with result") {
        val two = 2
        assertResult(two) { 1 + 1 }
    }
    ``` 
  
* intercept (Exception 발생 여부를 체크하여 주어진 Exception 발생시 통과)
    ```
    intercept[IllegalArgumentException] {
        someMethod()
    }
    
    # JUnit3 형식의 아래와 동일함
    try {
        someMethod()
        fail("Shouldn't be here") // 테스트 실패
    } catch {
        case _: IllegalArgumentException => // Expected so continue 
        case _ => fail("Unexpected exception thrown") // 테스트 실패
    }
    ```
    
## Deliberately failing tests (의도적 실패 테스트)
```
fail()
fail("Failure message")
```
위의 구문은 TestFailException을 발생시킨다.

## Assumptions (가정)
기본 Assertion 외에도 Assertion 내에서 특별한 기능을 사용할 수 있다. 이 특성은 일부 가정이 충족되지 않으면 테스트를 취소 할 수있는 방법을 제공하며, 이 기능에 의해 취소가 되더라도 테스트는 실패로 처리되지 않는다. 이는 테스트가 작동하기 위해 필요한 일부 전제 조건을 확인데 사용할 수 있다.
TestCanceledException 을 발생시키며, false 시 뒤의 message 를 리턴함 ("Duh!!")
```
assume(database.isAvailable(), "Duh!!")
```

## Canceling tests
cancle() 메서드는 TestCancelException을 발생시켜 의도적으로 테스트를 취소한다. cancel(message) 형태로도 사용 가능

## Failure messages and clues
assert, assertResult는 실패 단서에 대한 부분을 message 파라미터로 줄 수 있으나 intercept는 불가능한데 이는 withClue 를 통해 동일한 효과를 줄 수 있다. <br>
The withClue method will only modify the detailed message of an exception that is mixin the ModifiableMessage trait.
```
assert("Hello".length == 5, "Message")

assertResult(5, "Message") {"Hello".length}

withClue("Message") {
    intercept[IllegalArgumentException] {
        someMethod()
    }
}
```

## * Matchers
* ScalaTest는 Assertion 뿐 아니라 DSL(Domain-Specific Language) 도 지원함
* should 라는 단어를 사용하며, Matchers trait 을 추가하여 사용할 수 있다.
* FlatSpec 과 함께 사용
* 조건을 만족하지 못하는 경우 TestFailedException 을 발생함
* MustMatchers 는 should 대신 must 를 사용 (동일한 용법)
```
import org.scalatest._
class PacktSpec extends FlatSpec with Matchers

message should equal ("Hello World")
```

### Matchers 의 다양한 용법 

#### Matchers for equality
```
message should equal ("Hello World")
message should be ("Hello World")
message shouldBe "Hello World"
message should === ("Hello to World")
message shouldEqual "Hello World"
```

#### Matchers for instance and identity checks of objects
```
# Instance Type Check
message shouldBe a [String]
person should not be an [Animal]

names should be a [Seq[_]] ??

# Identity Check
obj1 should be theSameInstanceAs obj2
```

#### Matchers for size and length
```
# 어떠한 타입 T 라도 implicit Length[T] 가 가능하면 사용 가능
# size 또한 마찬가지로 implicit Size[T] 가 가능하면 사용 가능

message should have length 10
population should have size 200
```

#### Matching strings
```
# 일반 String Matching
message should startWith ("Hello")
message should endWith ("rld")
message should not include ("Batman")

# regex 사용한 Matching
message should endWith regex ("wor.d") -> regex 추가 필요
message should fullyMatch regex ("[A-Za-zs]+") -> s 대신 공백 또는 \\s 로 변경

# regex 와 requirement 조합 (regex 에 해당하는 값과 123 이 같아야만 통과)
"123zyx321" should startWith regex ("([\\d]+)" withGroups("123")) -> d 대신 \\d 로 변경
```

#### Matching greater and less than
```
# 어떠한 T 타입이라도 implicit Ordering[T] 를 구현하고 있으면 사용 가능 
number should be < 7
number should be <= 7
number should be >= 7
number should be > 7
```

#### Matching Boolean properties
```
# war 객체가 over 또는 isOver 를 구현하고 있어야 함
# earth 객체가 planet 또는 isPlanet 을 구현하고 있어야 함
# 해당 함수가 false 인 경우 TestFailedException 가 리턴되며 책과는 좀 다르게 리턴됨

war shouldBe 'over  -> War() was not over
earth should be a 'planet  -> Earth() was not a planet

# Range 를 가지는 number 의 경우 +- Operator 사용 가능
# Type 주의 필요 (Int, Double)
voltage should equal (12.0 +- 0.5)
voltage should be (12.0 +- 0.5)
voltage shouldBe 240 +- 10
```

#### Matching emptiness
```
# Option, String Length, Collection Size, Function(isEmpty) 에 다양하게 사용 가능 
None shouldBe empty
Some(1) should not be empty
"" shouldBe empty
new java.util.HashMap[Int, Int] shouldBe empty
new { def isEmpty = true} shouldBe empty
Array(1, 2, 3) should not be empty
```

#### Writing your own BeMatchers
사용자 정의 BeMatcher를 만들 수 있다. (뒤에 Custom Matcher 참고)
```
message shouldBe lowercase
```

### Some more Matchers
#### Matchers for containers
```
# 어떠한 T 타입이라도 implicit org.scalatest.enabler.Containing[T] 를 구현하고 있으면 사용 가능
# GenTraversable, Collection, Map, String, Array, Option 등

listOfNames should contain("Bob")

# oneOf, noneOf, alLeastOneOf ...
List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
Some(0) should contain noneOf (7, 8, 9)
(Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)

List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3) -> inOrder + Only (다른 값 존재하면 안됨)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)

# sortable object 에서 사용 가능
List(1, 2, 3) shouldBe sorted

val xs = List(1, 2, 3)
forAll (xs) { x => x should be < 10 } -> forAll 오류 ?

# 더 나이스 한 표현
all (xs) should be < 10
```
* all: 모든 요소들이 조건을 만족하면 성공
* atLeast: 조건을 만족하는 개수가 적어도 지정된 값을 넘으면 성공 
* atMost: 조건을 만족하는 개수가 많아도 지정된 값을 넘지 않으면 성공 
* between: 조건을 만족하는 개수가 최소/최대 값 사이에 존재하면 성공 
* every: all 과 동일하나 실패시 모든 실패 결과를 나열함 (나머지는 모두 처음 실패 케이스만 나열)
* exactly: 조건을 만조하는 개수가 지정된 값과 일치하면 성공

```
all (xs) should be > 0
atMost(2, xs) should be >= 4
atLeast(3, xs) should be < 5
between(2, 3, xs) should (be > 1 and be < 5)
exactly (2, xs) should be <= 2
every (xs) should be < 10
```

#### Combining Matchers with logical expressions
```
# and, or, not 을 조합한 Matchers

map should (contain key ("two") and not contain value (7))
(1 to 9) should (contain (7) or (contain (8) and have size (9))) <- 예제는 traversable 로 되어 있음
(8 to 16) should (contain (7) or (contain (8) and have size (9))) <- 예제는 traversable 로 되어 있음
map should (not be (null) and contain key ("ouch"))
```

#### Matching options
```
# Option 이 None 일때

option shouldEqual None
option shouldBe None
option should === (None)
option shouldBe empty

# Option 이 값을 가지고 있을때

option shouldEqual Some("hi")
option shouldBe defined
```

#### Matching properties
have를 이용해 객체의 퍼블릭 필드, 메서드, JavaBean-Style 의 get/is 메서드 속성을 검사할 수 있다.
```
book should have (
  'title ("Programming in Scala"),
  'author (List("Odersky", "Spoon", "Venners")),
  'pubYear (2008),
  'bestSeller (true)
)
```

#### Custom Matchers (create own Matchers, call endWithExtension)
```scala
import org.scalatest._
import matchers._

trait CustomMatchers {
  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
    def apply(left: java.io.File): MatchResult = { val name = left.getName
      MatchResult(
        name.endsWith(expectedExtension),
        s"""File $name did not end with extension "$expectedExtension"""",
        s"""File $name ended with extension "$expectedExtension""""
      )
    }
  }

  class LowerCaseMatcher(targetString: String) extends Matcher[String] {
    def apply(left: String): MatchResult = {
      MatchResult(
        targetString.toLowerCase == targetString,
        "is not lowercase",
        "lowercase"
      )
    }
  }

  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
  def lowercase(string: String) = new LowerCaseMatcher(string)
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers
```

#### Checking that a snippet of code does not compile
```
"val a: String = 1" shouldNot compile
"val a: String = 1" shouldNot typeCheck
"val a: Int = 1" should compile
```


## * Base test classes
스칼라테스트는 유사하거나 고유한 문제에 대한 솔루션에 중점을 둔 많은 경량 trait 으로 구성되어 문제 해결을 위해 다양하게 Mixin 하여 활용한다.
이런 경우를 위해 많은 경량 trait 이 Mixin 된 abstract class 를 정의하여 이를 활용하도록 하면 프로젝트의 모든 테스트에서 균일한 DSL 을 제공할 수 있으며,
이는 중복제거 및 빠른 컴파일을 가능하게 함
```scala
package com.packt
import org.scalatest._

abstract class UnitSpec extends FlatSpec
                           with Matchers
                           with OptionValues
                           with Inside
                           with Inspectors
                           
class DecimalBinarySpec extends UnitSpec {...}
class BeanSpec extends UnitSpec (---)
class BinaryToDecimalSpec extends UnitSpec {...}
``` 
대부분의 프로젝트들은 다양한 베이스 테스트클래스들을 갖기도 한다. (DbSpec, ActorSysSpec, DbActorSysSpec 등)

## * Test fixtures
Test fixture 는 클래스, 기타 라이브러리 및 아티팩트의 모음이고, 테스트에 필요한 파일, 소켓, 데이터베이스 연결 등이 될 수 있다. 
Clean Code 원칙에 따라 테스트에서 이러한 픽스쳐를 재사용하고 가능한 한 추상적으로 만들어야 한다.
예를 들어, 두 가지 테스트에서 데이터베이스와 통신해야하는 경우 두 데이터베이스에 동일한 데이터베이스 연결 코드를 재사용해야하는데, 
테스트에서 코드 중복이 많을수록 테스트에서 실제 리팩토링에 대한 부담이 더 커지게 된다.

ScalaTest 에서는 아래의 기술들을 권장하고 있음
* Reafactor using Scala
* Override 'withFixture'
* Mixin a 'before-and-after' trait

이런 기술로 부터 테스트간 종속성을 줄이고, 코드 중복을 줄여 더 쉽게 추론할 수 있고 병렬로 테스트 하는데도 문제 없도록 할 수 있다. 

각각의 권장 사용법 및 기술에 대한 설명
* Refactor using Scala when different tests need different fixtures
  * get-fixture methods
  * fixture-content objects
  * load-fixture methods
  
* Override withFixture when most or all tests need the same fixture
  * withFixture(No ArgsTest)

* mixin a before-and-after trait when you want an aborted suite, not a failed test, if the fixture code fails
  * BeforeAndAfter
  * BeforeAndAfterEach
  
### Calling get-fixture methods
* get-fixture 메소드는 하나 이상의 테스트에 동일한 가변 fixture 객체가 생성되어야 하고 fixture를 사용한 후 정리할 필요가 없을 때 사용
* fixture가 필요한 각 테스트 시작시 리턴 된 오브젝트를 로컬 변수에 저장하여 get-fixture 메소드를 호출 할 수 있음
* get-fixture 메소드는 호출 될 때마다 필요한 fixture 객체 (또는 여러 fixture 객체를 포함하는 holder 객체)의 새 인스턴스를 반환
* 가변 fixture 가 필요한 경우 매개변수를 전달해서 다른 fixture 를 구성할 수 있다.
```scala
package com.packt.examples.getfixture

import org.scalatest.FlatSpec
import collection.mutable.ListBuffer

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
``` 

### Instantiating fixture-context objects
* 테스트 마다 다른 fixture 가 필요한 경우 또는 조합이 필요한 경우 유용한 방법
* fixture 객체를 fixture-context object 의 인스턴스 변수로 정의하는 것이며, 인스턴스화는 테스트 본문을 형성함
* get-fixture 기법과 마찬가지로 사용 후 Clean Up 이 필요 없는 경우에 사용
* 이 기법을 사용하기 위해 Trait 또는 Class 에서 fixture 객체로 초기화 된 인스턴스 변수를 정의한 후
* 각 테스트에서 테스트에 필요한 fixture 오브젝트 만 포함된 오브젝트를 인스턴스화 하여 사용
* Trait 을 사용하면 각 테스트에 필요한 픽스쳐 개체 만 함께 Mixin 할 수 있지만 클래스를 사용하면 생성자를 통해 데이터를 전달하여 픽스쳐 개체를 구성 해야 함 
* 아래 예는 fixture 오브젝트가 두 Trait 으로 분할되는 예이며 각 테스트는 필요한 트레잇을 함께 Mixin 하여 사용
```scala
package com.packt.examples.fixturecontext

import collection.mutable.ListBuffer
import org.scalatest.FlatSpec
 
class ExampleSpec extends FlatSpec {
  trait Builder {
    val builder = new StringBuilder("ScalaTest is ")
  }
  
  trait Buffer {
    val buffer = ListBuffer("ScalaTest", "is")
  }

  // This test needs the StringBuilder fixture
  "Testing" should "be productive" in new Builder {
    builder.append("productive!")
    assert(builder.toString === "ScalaTest is productive!")
  }

  // This test needs the ListBuffer[String] fixture
  "Test code" should "be readable" in new Buffer {
    buffer += ("readable!")
    assert(buffer === List("ScalaTest", "is", "readable!"))
  }

  // This test needs both the StringBuilder and ListBuffer
  it should "be clear and concise" in new Builder with Buffer {
    builder.append("clear!")
    buffer += ("concise!")
    assert(builder.toString === "ScalaTest is clear!")
    assert(buffer === List("ScalaTest", "is", "concise!"))
  }
}
```

### Overriding withFixture(NoArgTest)
* get-fixture 방법과 fixture-context 객체 접근 방식은 각 테스트가 시작될 때 fixture를 설정하여 사용하지만 뒤처리는 하지 않음
* 테스트 시작 또는 종료시 사이드 이펙트를 수행해야하고, 실제로 fixture 오브젝트를 테스트에 전달할 필요가 없는 경우
* override withFixture 를 통해 처리
```scala
// Default implementation in trait Suite
protected def withFixture(test: NoArgTest) = {
  test()
}
```

```scala
// Your implementation
override def withFixture(test: NoArgTest) = {
  // Perform setup
  try super.withFixture(test) // Invoke the test function
  finally {
    // Perform cleanup
  }
}
```

* 아래 예를 기준으로 FlatSpec 은 FlatSpecLike 를 Mixin 했고, FlatSpecLike 는 여러 Trait 을 Mixin 했는데 이중 Suite 에 withFixture 함수가 존재함
* 실패했을때 현재 디렉토리의 파일들을 표출함
```scala
package com.packt.examples.noargtest

import java.io.File
import org.scalatest.{Failed, FlatSpec}

class ExampleSpec extends FlatSpec {
  override def withFixture(test: NoArgTest) = {
    super.withFixture(test) match {
      case failed: Failed =>
        val currDir = new File(".")
        val fileNames = currDir.list()
        info("Dir snapshot: " + fileNames.mkString(", "))
        failed
      case other => other
    }
  }

  "This test" should "succeed" in {
    assert(1 + 1 === 2)
  }

  it should "fail" in {
    assert(1 + 1 === 3)
  }
}
```

### Calling loan-fixture methods
* 테스트코드를 매개변수로 가지는 fixture 함수를 빌려와서 활용하는 기법을 calling loan-fixture methods 라 함
* method 에서 fixture 에 대한 전처리, 후처리를 처리하고 test code 를 fixture 에 매개변수로 전달
* fixture 에 side effect 가 있는... (파일 또는 데이터베이스의 생성/입력 등) 경우 각각의 동작이 독립적으로 수행되도록 처리하면 병렬 동작도 가능
* 아래 예제는 두 개의 fixture (데이터베이스와 파일)를 사용하는 세 가지 테스트 임
둘 다 이후 정리가 필요하므로 각각 론 고정 방법을 통해 제공됩니다.
```scala
package com.packt.examples.loanfixture

import org.scalatest.FlatSpec
import java.util.UUID.randomUUID
import java.io._
import DbServer._

class ExampleSpec extends FlatSpec {
  def withDatabase(testCode: Db => Any) {
    val dbName = randomUUID.toString
    val db = createDb(dbName)

    // create the fixture
    try {
      db.append("ScalaTest is ")
      // perform setup
      testCode(db)
      // "loan" the fixture to the test
    }
    finally removeDb(dbName)
    // clean up the fixture
  }

  def withFile(testCode: (File, FileWriter) => Any) {
    val file = File.createTempFile("hello", "world")

    // create the fixture
    val writer = new FileWriter(file)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      testCode(file, writer)
      // "loan" the fixture to the test
    }
    finally writer.close()
    // clean up the fixture
  }

  // This test needs the file fixture
  "Testing" should "be productive" in withFile {
    (file, writer) =>
      writer.write("productive!")
      writer.flush()
      assert(file.length === 24)
  }

  // This test needs the database fixture
  "Test code" should "be readable" in withDatabase {
    db => db.append("readable!")
    assert(db.toString === "ScalaTest is readable!")
  }

  // This test needs both the file and the database
  it should "be clear and concise" in withDatabase {
    db => withFile { (file, writer) =>
      // loan-fixture methods compose
      db.append("clear!")
      writer.write("concise!")
      writer.flush()
      assert(db.toString === "ScalaTest is clear!")
      assert(file.length === 21)
    }
  }
}

import java.util.concurrent.ConcurrentHashMap

object DbServer {
  // Simulating a database server
  type Db = StringBuffer
  private val databases = new ConcurrentHashMap[String, Db]
  def createDb(name: String): Db = {
    val db = new StringBuffer
    databases.put(name, db)
    db
  }

  def removeDb(name: String) { databases.remove(name) }
}
```

### Overriding withFixture(OneArgTest)
* 모든 또는 대부분의 테스트에 동일한 fixture 가 필요한 경우 fixture.FlatSpec을 상속받아 withFixture(OneArgTest)로 재정의하여 사용하는 것으로 loan-fixture method 의 boilerplate 를 피할 수 있다.
* Fixture.FlatSpec의 각 테스트는 fixture 를 매개 변수로 사용하여 테스트에 전달할 수 있음
* FixtureParam을 지정하여 fixture 매개 변수의 유형을 표시하고 OneArgTest를 사용하는 withFixture 메소드로 구현해야합니다.  
* 테스트 함수를 test(theFixture) 처럼 직접 호출하는 대신 withFixture(NoArgTest) 를 사용하여 테스트 함수를 호출하도록하는 것이 좋다.
* 이렇게하려면 OneArgTest를 NoArgTest로 변환해야 하는데 이는 fixture 오브젝트를 OneArgTest 의 toNoArgTest 메소드에 전달하여이를 수행 가능
* 이 예제에서 테스트에는 File과 FileWriter라는 두 개의 fixture 객체가 필요 -> 이러한 상황에서 FixtureParam 유형을 오브젝트를 포함하는 튜플로 정의하거나 이 예제에서 수행된 것처럼 오브젝트를 포함하는 케이스클래스로 정의할 수 있다.
```scala
package com.packt.examples.oneargtest

import java.io.{File, FileWriter}
import org.scalatest.fixture

class ExampleSpec extends fixture.FlatSpec {

  case class FixtureParam(file: File, writer: FileWriter)

  def withFixture(test: OneArgTest) = {
    val file = File.createTempFile("hello", "world") // create the fixture
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      withFixture(test.toNoArgTest(theFixture))
      // "loan" the fixture to the test
    } finally writer.close()
    // clean up the fixture
  }

  "Testing" should "be easy" in { f =>
    f.writer.write("easy!")
    f.writer.flush()
    assert(f.file.length === 18)
  }

  it should "be fun" in { f =>
    f.writer.write("fun!")
    f.writer.flush()
    assert(f.file.length === 17)
  }
```

### Mixing in BeforeAndAfter
* 지금까지 확인한 방법은 모두 테스트 과정에서 진행되는 일이라 정비과정에서의 실패 또한 테스트 실패로 이어짐
* 이와는 별개로 정리중에 발생하는 이슈와 테스트중 발생하는 이슈를 구분하는 방법으로 활용됨
* 각 테스트의 실행 이전, 실행 이후에 수행돼야 하는 일들을 정의
* 각 테스트 코드간 통신은 인스턴스 변수를 재할당하거나 인스턴스에 val 로 mutable 객체를 생성하는 것처럼 사이드 이펙트를 이용함
* 따라서, 테스트를 병령로 실행할 수 없다.
* 이를 해결하기 위해서 ScalaTest 의 ParallelTestExecution Trait 은 테스트 클래스당 하나의 인스턴스를 할당하여 동작함
* BeforeAndAfter는 테스트 전후에 코드를 실행하는 최소한의 방법을 제공하지만 실행 순서가 명확하지 않기 때문에 스택 가능한 특성을 사용하도록 설계되지 않음
```scala
package com.packt.examples.beforeandafter

import org.scalatest.{BeforeAndAfter, FlatSpec}
import collection.mutable.ListBuffer

class ExampleSpec extends FlatSpec with BeforeAndAfter {
  val builder = new StringBuilder
  val buffer = new ListBuffer[String]

  before {
    builder.append("ScalaTest is ")
  }

  after {
    builder.clear()
    buffer.clear()
  }

  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
  }
}
```
### Composing fixtures by stacking traits
* 대규모 프로젝트에서 팀은 종종 테스트 클래스가 여러 fixture 의 다른 조합 또는 다른 순서로 초기화 (및 정리) 해야 하는 상황이 존재
* ScalaTest 에서 이를 수행하는 좋은 방법은 개별 fixture 를 스택이 가능한 Trait 패턴을 사용하여 구성할 수있는 Trait 으로 인수 분해하는 것
* 예를 들어, 여러 가지 특성에 withFixture 메소드를 배치하면 각각 super.withFixture를 호출하여 수행
* 이전 예제에서 사용 된 StringBuilder 및 ListBuffer[String] fixture 가 Builder 및 Buffer라는 두 개의 스택 가능 fixture Trait 으로 분해 된 예는 다음과 같다.
```scala
package com.packt.examples.composingwithfixture

import org.scalatest._
import collection.mutable.ListBuffer

trait Builder extends SuiteMixin {
  this: Suite =>
  val builder = new StringBuilder

  abstract override def withFixture(test: NoArgTest) = {
    builder.append("ScalaTest is ")
    try super.withFixture(test)
    // To be stackable, must call super.withFixture
    finally builder.clear()
  }
}

trait Buffer extends SuiteMixin {
  this: Suite =>
  val buffer = new ListBuffer[String]
  
  abstract override def withFixture(test: NoArgTest) = {
    try super.withFixture(test)
    // To be stackable, must call super.withFixture
    finally buffer.clear()
  }
}

class ExampleSpec extends FlatSpec with Builder with Buffer {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }
  
  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}
```
위의 ExampleSuite는 Builder와 Buffer Trait 을 혼합하여 두 fixture 를 가져오는데 각 fixture 는 각 테스트 전에 초기화되고 이후에 정리됨 <br>
특성이 혼합 된 순서에 따라 실행 순서가 결정되며, 이 경우 Builder 는 Buffer 에 super 이고, Buffer 가 Builder에 super 가되도록하려면 아래와 같이 혼합 순서를 전환하면 됨
```
class Example2Suite extends Suite with Buffer with Builder

# 한개만 Mixin 하는 경우
class Example2Suite extends Suite with Builder
```
* 쌓을 수있는 fixture Trait 을 만드는 또 다른 방법은 BeforeAndAfterEach 또는 BeforeAndAfterAll 특성을 확장하는 것
* BeforeAndAfterEach에는 각 테스트 전에 실행되는 beforeEach 메소드 (JUnit의 SetUp)와 이후에 실행될 AfterEach 메소드 (JUnit의 tearDown) 가 있다.
* 마찬가지로 BeforeAndAfterAll에는 모든 테스트 전에 실행될 beforeAll 메소드와 모든 테스트 후에 실행될 afterAll 메소드가 있다.
* 아래는 이전에 표시된 예제의 Fixture 대신 BeforeAndAfterEach 메서드를 사용하도록 다시 작성된 경우의 모습
```scala
package com.packt.examples.composingbeforeandaftereach

import org.scalatest._
import collection.mutable.ListBuffer

trait Builder extends BeforeAndAfterEach {
  this: Suite =>
  val builder = new StringBuilder

  override def beforeEach() {
    builder.append("ScalaTest is ")
    super.beforeEach()
    // To be stackable, must call super.beforeEach
  }

  override def afterEach() {
    try super.afterEach()
    // To be stackable, must call super.afterEach
    finally builder.clear()
  }
}

trait Buffer extends BeforeAndAfterEach {
  this: Suite =>
  val buffer = new ListBuffer[String]

  override def afterEach() {
    try super.afterEach()
    // To be stackable, must call super.afterEach
    finally buffer.clear()
  }
}

class ExampleSpec extends FlatSpec with Builder with Buffer {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}
```
* withFixture와 동일한 순서를 얻으려면 앞의 예제와 같이 각 beforeEach 메서드의 끝에 super.beforeEach 호출을, 각 afterEach 메서드의 시작 부분에 super.afterEach 호출을 배치
* try 블록에서 super.afterEach를 호출하고 이전 예제와 같이 finally 에서 Clean Up 을 수행하는 것이 좋음
* afterEach 에서 try 가 사용된 이유는 super.afterEach가 예외를 throw 하더라도 Clean Up 코드가 수행되도록 하기위해서 이다.
* BeforeAndAfterEach를 확장하는 스태킹 Trait 과 Fixture로 구현하는 특성의 차이점
  * BeforeAndAfterEach에서 각각의 테스트 전후에 SetUp 및 CleanUp 이 발생하지만
  * withFixture 에서는 테스트 시작 및 종료시 SetUp 및 CleanUp 발생
  * 따라서 withFixture 메소드가 예외와 함께 갑자기 완료되면 실패한 테스트로 간주되지만, BeforeAndAfterEach의 beforeEach 또는 afterEach 메소드 중 하나가 갑자기 완료되면 실패가 아닌 중단된것으로 간주되어 SuiteAborted 이벤트가 발생

## Problem statement
* 이전 장의 기본 변환에 대한 예제를 10 진수에서 16 진수로 또는 그 반대로 변환 할 수 있도록 확장
* 시나리오 1 : 주어진 10진수 A 를 16진수로 변경하여 계산된 16진수값과 같은지 체크
* 시나리오 2 : 주어진 16진수 X 를 10진수로 변경하여 계산된 10진수값과 같은지 체크
* 시나리오 3 : 주어진 10진수 A 를 16진수로 변환 후 다시 10진수로 변환했을때 최초 10진수 A 와 같은지 체크
