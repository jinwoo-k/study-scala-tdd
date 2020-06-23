# Chapter3. Clean Code Using ScalaTest
이 장에서는 클린 테스트코드를 작성하기 위한 몇가지 예제를 살펴볼것이다. 또한 아래와 같은 ScalaTest의 기능들에 대해 살펴본다.
* Assertion
* Matchers
* Base test classes
* Test fixture

## Assertion
* assert
    ```
    test("one plus one") {
        assert(1 + 1 == two)
        assert(1 + 1 != three)
    }
    ```
* assertResult
    ```
    test("one plus one with result") {
        val two = 2
        assertResult(two) { 1 + 1 }
    }
    ``` 
* intercept
    ```
    intercept[IllegalArgumentException] {
        someMethod()
    }
    
    # equivalent
    try {
        someMethod()
        fail("Shouldn't be here") }
    catch {
        case _: IllegalArgumentException => // Expected so continue 
        case _ => fail("Unexpected exception thrown")
    }
    ```
    
### Deliberately failing tests (의도적 실패 테스트)
```
fail()
fail("Failure message")
```
위의 구문은 TestFailException을 발생시킨다.

### Assumptions
가정이 충족되지 않으면 테스트를 취소할 수 있으며 이는 테스트 실패와 다르다. TestCanceledException 을 발생시킨다. 
```
assume(database.isAvailable(), "Duh!!")
```

### Canceling tests
cancle() 메서드는 TestCancelException을 발생시켜 의도적으로 테스트를 취소한다.

### Failure messages and clues
assert, assertResult는 실패 단서에 대한 부분을 파라미터로 줄 수 있으나 intercept는 그렇지 않다. 이런경우 withClue 를 통해 단서를 줄 수 있다.
```
assert("Hello".length == 5, "Message")

assertResult(5, "Message") {"Hello".length}

withClue("Message") {
    intercept[IllegalArgumentException] {
        someMethod()
    }
}
```

## Matchers
ScalaTest는 기본 assertion 외에도 should 키워드를 사용하는 DLS를 이용한 assertion을 제공한다.
이것이 매처와 결합된다.
```
import org.scalatest._
class PacktSpec extends FlatSpec with Matchers

message should equal ("Hello World")
```
결과가 동일하지 않은 경우 TestFailedException을 발생시킨다.

### Matchers for equality
```
message should be ("Hello World")
message shouldBe "Hello World"
message should equal ("Hello World")
message should === ("Hello to World")
message shouldEqual "Hello World"
```

### Matchers for instance and identity checks of objects
```
message shouldBe a [String]
person should not be an [Animal]

# type erased
names should be a [Seq[_]]

obj1 should be theSameInstanceAs obj2
```

### Matchers for size and length
```
message should have length 10
population should have size 200
```

### Matching strings
```
message should startWith ("Hello")
message should endWith ("rld")
message should not include ("Batman")

# using regexp
message should endWith ("wor.d")

message should fullyMatch regex ("[A-Za-zs]+")

"123zyx321" should startWith regex ("([d]+)" withGroups("123")
```

### Matching greater and less than
```
number should be < 7
number should be <= 7
number should be >= 7
number should be > 7
```

### Matching Boolean properties
```
voltage should equal (12.0 +- 0.5)
voltage should be (12.0 +- 0.5)
voltage shouldBe 240 +- 10
```

### Matching emptiness
```
None shouldBe empty
Some(1) should not be empty
"" shouldBe empty
new java.util.HashMap[Int, Int] shouldBe empty
new { def isEmpty = true} shouldBe empty
Array(1, 2, 3) should not be empty
```

### Writing your own BeMatchers
사용자 정의 BeMatcher를 만들 수 있다.
```scala
import org.scalatest._ import matchers._
trait CustomMatchers {
    class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
        def apply(left: java.io.File) = {
            val name = left.getName MatchResult(
              name.endsWith(expectedExtension),
              s"""File $name did not end with extension "$expectedExtension"""", s"""File $name ended with extension "$expectedExtension""""
            )
        }
    }
    def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers


// use
file should not endWithExtension "txt"
```

dynamic matcher
```
val hidden = 'hidden
new File("file.txt") shouldBe hidden
```

### Matchers for containers
```
listOfNames should contain("Bob")

List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
Some(0) should contain noneOf (7, 8, 9)

(Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)

List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)

List(1, 2, 3) shouldBe sorted

val xs = List(1, 2, 3)
forAll (xs) { x => x should be < 10 }

# same
all (xs) should be < 10
```
inspectors: all, atLeast, atMost, between, every, exactly
```
all (xs) should be > 0
atMost(2, xs) should be >= 4
atLeast(3, xs) should be < 5
between(2, 3, xs) should (be > 1 and be < 5)
exactly (2, xs) should be <= 2
every (xs) should be < 10
```

### Combining Matchers with logical expressions (and, or)
```
should (contain key ("two") and not contain value (7))
traversable should (contain (7) or (contain (8) and have size (9)))
map should (not be (null) and contain key ("ouch"))
```

### Matching options
```
option shouldEqual None
option shouldBe None
option should === (None)
option shouldBe empty
```

### Matching properties
have를 이용해 객체의 퍼블릭 필드 메서드, get/is 메서드 속성을 검사할 수 있다.
```
book should have (
'title ("Programming in Scala"),
'author (List("Odersky", "Spoon", "Venners")), 'pubYear (2008)
)
```

### Checking that a snippet of code does not compile
```
"val a: String = 1" shouldNot compile
"val a: String = 1" shouldNot typeCheck
"val a: Int = 1" should compile
```

## Base test classes
스칼라테스트는 다양한 "하나의 문제를 해결하기 위한 경량 트레잇"을 포함하며 우리는 그것을 믹스해 사용한다.
다양한 코드를 반복하는 대신 프로젝트에서 많이 사용하는 기능을 함께 혼합하는 추상 기본 클래스를 이용하는게 좋다.
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

많은 프로젝트는 다양한 베이스 테스트클래스를 갖는다. DbSpec, ActorSysSpec, DbActorSysSpec 등..

## Test fixtures
Test fixture는 클래스, 기타 라이브러리 및 아티팩트의 집합니다. 테스트에 필요한 파일, 소켓, 데이터베이스 연결등이다. 
클린코드 원칙에 의하면 이러한 fixture를 가능한 한 추상화 하라 한다. 예를들어 다양한 테스트에서 DB접근이 필요한 경우 DB연결 코드가 중복되지 않게 해야한다.

스칼라테스트는 아래의 기술들을 권장한다.
* Reafactor using scala
* Override withFixture
* Mixin a before-and-after trait

이런 기술은 테스트 간 의존성 감소와 코드 중복을 줄인다. 테스트에서 공유 변이가능 상태를 제거하면 테스트 코드가 더 쉽게 추론되고 병렬 테스트가 가능해진다. 

### Calling get-fixture methods
get-fixture 메서드는 하나 이상의 테스트에 동일한 가변 픽스쳐 객체가 생성되어야 하고 픽스쳐를 사용한 후 정리할(cleaned up) 필요가 없을 때 사용된다.
픽스처가 필요한 각 테스트 시작시 리턴 된 오브젝트를 로컬 변수에 저장하여 get-fixture 메소드를 호출 할 수 있습니다.
get-fixture 메소드는 호출 될 때마다 필요한 픽스쳐 객체 (또는 여러 픽스쳐 객체를 포함하는 holder 객체)의 새 인스턴스를 반환합니다. 예를 들면 다음과 같습니다.
```scala
package com.packt.example.getfixture
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
}
``` 

### Instantiating fixture-context objects
다른 테스트에서 픽스쳐 객체의 다른 조합이 필요할 때 특히 유용한 대체 기술은 픽스쳐 객체를 픽스쳐 컨텍스트 객체의 인스턴스 변수로 정의하는 것이며, 인스턴스화는 테스트 본문을 형성합니다.
같은 get-fixture 메소드와 fixture-context 객체는 사용 후에 픽스쳐를 정리할 필요가 없는 경우에만 적합합니다.
이 기법을 사용하기 위해 트레잇 또는 클래스에서 픽스쳐 객체로 초기화 된 인스턴스 변수를 정의합니다.
그런 다음 각 테스트에서 테스트에 필요한 픽스쳐 오브젝트 만 포함 된 오브젝트를 인스턴스화 하십시오.
트레잇을 사용하면 각 테스트에 필요한 픽스쳐 개체 만 함께 혼합 할 수 있지만 클래스를 사용하면 생성자를 통해 데이터를 전달하여 픽스쳐 개체를 구성 할 수 있습니다. 
다음은 픽스쳐 오브젝트가 두 트레잇으로 분할되는 예이며 각 테스트는 필요한 트레잇을 함께 혼합합니다.

```scala
package com.packt.example.fixturecontext
import collection.mutable.ListBuffer import org.scalatest.FlatSpec
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
get-fixture 방법과 fixture-context 객체 접근 방식은 각 테스트가 시작될 때 픽스쳐를 설정하는 것을 처리하지만, 테스트가 끝날 때 픽스쳐구를 정리하는 문제는 다루지 않습니다.
테스트 시작 또는 종료시 사이드 이펙트를 수행해야하고 실제로 픽스쳐 오브젝트를 테스트에 전달할 필요가 없는 경우 트레잇 스위트에 정의 된 ScalaTest의 수명주기 메소드 중 하나 인 Fixture (NoArgTest)를 재정의 할 수 있습니다. 

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

```scala
package com.packt.example.noargtest
import java.io.File import org.scalatest._

class ExampleSpec extends FlatSpec {
    override def withFixture(test: NoArgTest) = {
      super.withFixture(test) match {
        case failed: Failed =>
            val currDir = new File(".")
            val fileNames = currDir.list()
            info("Dir snapshot: " + fileNames.mkString(", ")) failed
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
픽스쳐 객체를 테스트에 전달하고 테스트가 끝날 때 정리를 수행해야하는 경우 론 패턴을 사용해야합니다.
다른 테스트에서 정리가 필요한 다른 픽스쳐가 필요한 경우, loan-fixture 메서드를 작성하여 론 패턴을 직접 구현할 수 있습니다.
론 담합 방법은 본문이 테스트 코드의 일부 또는 전부를 구성하는 함수를 사용합니다.
픽스쳐를 생성하고 함수를 호출하여 테스트 코드로 전달한 다음 함수가 반환 된 후 픽스쳐를 정리합니다.
다음 예제는 두 개의 픽스쳐 (데이터베이스와 파일)를 사용하는 세 가지 테스트를 보여줍니다.
둘 다 이후 정리가 필요하므로 각각 론 고정 방법을 통해 제공됩니다.
(이 예제에서 데이터베이스는 StringBuffer로 시뮬레이션됩니다) :
```scala
package com.packt.examples.loanfixture
import java.util.concurrent.ConcurrentHashMap

object DbServer {
	// Simulating a database server
	type Db = StringBuffer

	private val databases = new ConcurrentHashMap[String, Db]

	def createDb(name: String): Db = {
		val db = new StringBuffer databases.put(name, db)
		db
	}

	def removeDb(name: String) {
		databases.remove(name)
	}
}


import org.scalatest.FlatSpec
import DbServer._
import java.util.UUID.randomUUID import java.io._

class ExampleSpec extends FlatSpec {
	def withDatabase(testCode: Db => Any) {
		val dbName = randomUUID.toString val db = createDb(dbName)
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
			writer.write("ScalaTest is ") // set up the fixture testCode(file, writer)
			// "loan" the fixture to the test
		}
		finally writer.close()
		// clean up the fixture
	}

	// This test needs the file fixture
	"Testing" should "be productive" in withFile { (file, writer) =>
		writer.write("productive!")
		writer.flush()
		assert(file.length === 24)
	}

	// This test needs the database fixture
	"Test code" should "be readable" in withDatabase { db =>
		db.append("readable!")
		assert(db.toString === "ScalaTest is readable!")
	}

	// This test needs both the file and the database
	it should "be clear and concise" in withDatabase { db =>
		withFile { (file, writer) =>
			// loan-fixture methods compose db.append("clear!")
			writer.write("concise!")
			writer.flush()
			assert(db.toString === "ScalaTest is clear!")
			assert(file.length === 21)
		}
	}
}
```

### Overriding withFixture (OneArgTest)
모든 또는 대부분의 테스트에 동일한 픽스쳐가 필요한 경우 fixture.FlatSpec을 사용하고 Fixture (OneArgTest)로 재정의하는 loan-fixture method의 boilerplate를 피할 수 있습니다. Fixture.FlatSpec의 각 테스트는 픽스쳐를 매개 변수로 사용하여 픽스쳐를 테스트에 전달할 수 있습니다. FixtureParam을 지정하여 픽스쳐 매개 변수의 유형을 표시하고 OneArgTest를 사용하는 Fixture 메소드로 구현해야합니다. 이 withFixture 방법은 1-arg 테스트 기능을 호출하는 역할을 하므로 실제 테스트 전에 테스트 하니스 설정을 수행하거나 테스트 후 하우스 키핑 또는 클린업을 수행하고 픽스처를 호출하고 테스트 기능에 전달할 수 있습니다.
withFixture (NoArgTest)를 정의하는 트레잇의 스태킹을 활성화합니다. 테스트 함수를 직접 호출하는 대신 Fixture (NoArgTest)를 사용하여 테스트 함수를 호출하도록하는 것이 좋습니다. 이렇게하려면 OneArgTest를 NoArgTest로 변환해야합니다. 픽스쳐 오브젝트를 OneArgTest의 toNoArgTest 메소드에 전달하여이를 수행 할 수 있습니다. 즉, test (theFixture)를 작성하는 대신 다음과 같이 작성하여 테스트 인스턴스 호출에 대한 책임을 동일한 인스턴스의 withFixture (NoArgTest) 메소드에 위임해야합니다.

```
package com.packt.examples.oneargtest
import org.scalatest.fixture import java.io._

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
}

```

이 예제에서 테스트에는 실제로 File과 FileWriter라는 두 개의 픽스쳐 객체가 필요했습니다. 이러한 상황에서 FixtureParam 유형을 오브젝트를 포함하는 튜플로 정의하거나이 예제에서 수행 된 것처럼 오브젝트를 포함하는 케이스 클래스를 정의 할 수 있습니다.

### Mixing in BeforeAndAfter
각 테스트 이전/이후에 수행돼야 하는 일을 정의

```scala
package com.packt.examples.beforeandafter
import org.scalatest._
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

각 테스트 코드간 통신은 인스턴스 변수를 재할당하거나 인스턴스에 val 로 mutable 객체를 생성하는 것처럼 사이드 이펙트를 이용하는 것 이다. 이렇게 하면 테스트를 병렬로 실행할 수 없다. 이러한 특성때문에 ScalaTest 의 ParallelTestExecution 트레잇은 테스트 클래스당 하나의 인스턴스를 할당한다.
BeforeAndAfter는 테스트의 실행 순서를 보장하지 않는다. 이를 주의해야한다.

 


<br>
<br>
<br>
<br>
<br>
<br>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
<br><br><br><br><br><br><br><br><br><br><br>


asdf