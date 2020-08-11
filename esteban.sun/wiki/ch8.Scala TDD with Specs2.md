# Chapter8. Scala TDD with Specs2
Specs2는 Scala의 실행 가능 사양을 작성하는 데 사용되는 또 다른 인기있는 라이브러리입니다. 
Specs2는 ScalaTest 보다는 반대 부분에 초점을 맞추고 있으며, 그것은 또한 다른 관점을 가지고 있습니다. 
Scala 코드에 대한 테스트를 작성하는 또 다른 도구이며 이 장에서는 다음을 살펴볼 것입니다.
* Specs2 소개
* Specs2와 ScalaTest의 차이점 Specs2
* Setting up Specs2
* Unit specifications (단위 명세)
* Acceptance specification (기능 명세)
* Matchers
* 참조 : https://etorreborre.github.io/specs2/website/SPECS2-4.10.0/quickstart.html

## * Specs2 소개
ScalaTest와 마찬가지로 Specs2는 SBT에서도 작동합니다. Specs2와 ScalaTest에는 유사점과 차이점이 있습니다. Specs2와 ScalaTest의 몇 가지 고유 한 차이점은 개발자가 필요한 기능에 따라 하나를 선택할 수 있도록합니다.

## * Specs2와 ScalaTest의 차이점 Specs2
* ScalaTest와 Specs2의 주요 차이점은 다음과 같습니다.
  * Specs2의 전체 테스트 구조는 ScalaTest의 구조와 다릅니다.
  * Specs2에는 구문이 다른 다른 Matcher 세트가 있습니다.
  * Specs2 테스트는 주로 행동 기반 개발 (BDD) 전용이지만 ScalaTest 테스트는 보다 일반적입니다.
  * ScalaTest는 훨씬 더 많은 선택과 융통성을 제공합니다. 예를 들어 ScalaTest에서 BDD와 유사한 Specs2를 작성하려면 ShouldMatchers 또는 MustMatcher와 함께 Spec, FeatureSpec, WordSpec, FlatSpec 및 GivenWhenThen 특성을 사용할 수 있습니다. 이것은 개발자가 자신의 스타일의 작성 사양을 따를 수있는 더 많은 유연성을 제공합니다.
  * Specs2는 ScalaTest보다 훨씬 많은 수의 Matcher를 가지고 있습니다. 그들 대부분은 특별한 필요를 위한 것입니다. 예를 들어 Specs2에서 다음과 같이 말할 수 있습니다.
    * aFile mustBe aDirectory
  * Specs2에 java.io.Files에 대한 개별 Matcher가있는 경우 ScalaTest에는 유사한 Matcher가 없습니다.
  * ScalaTest와 Spec의 또 다른 차이점은 implicit conversion 입니다. ScalaTest에는 === 연산자를 사용하여 수행하는 암시적 변환이 하나뿐입니다. 그러나 이것은 한 줄의 코드를 사용하여 ScalaTest에서 끌 수 있습니다. 이것은 ScalaTest에서 무료로 얻을 수있는 유일한 암시적 변환입니다. 다른 특성을 혼합하여 사용할 수있는 다른 암시적 변환이 있습니다. 이 Specs2와는 반대로 Specification 클래스를 확장하는 것만으로도 수십 개의 암시 적 변환을 제공합니다. 실제로 이것은 큰 차이를 만들지 않습니다. 유일한 차이점은 명시 적 변환없이 테스트 코드를 더 쉽게 읽을 수 있다는 것입니다.
  * Specs2에는 ScalaTest와 함께 제공되는 기본 5 개 연산자보다 더 많은 연산자 모음이 있습니다. 다음 기본 연산자 만 정의합니다.
    * scalaTest : ===, >, <, >=, <=
    * specs2 : ->-, >>, |, |>, !, ^^^
  * Specs2에는 데이터 테이블이 있습니다. 이를 통해 속성 기반 테스트와 마찬가지로 테스트를 주도 할 수있는 여러 예제를 사용할 수 있습니다.
  * Specs2 테스트는 별도의 스레드에서 동시에 실행할 수 있다는 점에서 ScalaTest와 다릅니다.
  
결론적으로, Specs2와 ScalaTest는 거의 동일한 결과를 얻을 수 있습니다. 서로를 사용하는 것은 선호도의 문제입니다.

## * Setting up Specs2
* SBT에서 실행할 수 있도록 Specs2를 설정하려면 build.sbt에 다음 종속성 및 해결 프로그램을 추가해야합니다.
* 또는 resolver 에 maven 저장소를 설정하여 사용할 수도 있다.
    ```
    version := "1.0.0"
    name := "Specs2 Setup"
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.4" % "test"
    scalacOptions in Test ++= Seq("-Yrangepos")
    ```
## * 스타일
* Specification 에는 일반적으로 다음 두 가지가 포함됩니다.
  * 테스트중인 애플리케이션 코드의 기능을 설명하는 비공식 텍스트
  * 테스트에 대한 입력을 설명하고 출력을 예상 출력과 비교하는 스칼라 코드
* Specs2는 이를 수행하는 두 가지 방법을 제공합니다.
  * 모든 비공식 텍스트를 한곳에 작성하고 모든 Scala 코드를 다른 곳에 작성할 수 있습니다. 이 스타일의 명세를 "acceptance" Specification 라고합니다. 텍스트가 한 곳에 있기 때문에 개발자가 아닌 사람이 테스트를 읽고 명세을 승인하는 것이 훨씬 쉽습니다.
  * 또는 Scala 코드와 텍스트를 서로 인터리브 할 수 있습니다. 이 구조는 xUnit과 같은 전통적인 단위 테스트 프레임 워크와 더 유사합니다. 이를 "unit" Specification 라고 합니다.
* 두 스타일을 모두 사용하는 데는 장단점이 있습니다. "acceptance" Specification 는 스토리로 읽기가 더 간단하지만
  텍스트와 코드. 또한 테스트 본문을 보유하는 is 메서드를 작성해야합니다. 텍스트가 코드 바다에서 손실되는 경향이 있으므로 "unit" Specification 를 탐색하는 것이 더 쉽습니다. 이러한 스타일을 더 자세히 살펴 보겠습니다.

## * Unit Specifications (단위 명세)
* unit Specification 는 일반적으로 테스트 할 애플리케이션 코드를 설명하는 문자열로 시작하며 should 가 이어집니다.
* 이것은 ScalaTest와 매우 유사합니다. 다음 블록에는 실제 테스트 코드 블록이 차례로 나오는 하나 이상의 테스트 설명이 있습니다.
    ```
    class FootballTeamsUnitSpec extends Specification {
      "A Football Team" should {
        """have a getGoalKeeper function which
        selects the plater who has been earmarked to be the goalkeeper""" in {
          val firstTeam =
            new FootballTeam(
              "Gaurav's Team",
              2016,
              Some(
                List(
                  new Player("Tom", "Midfielder"),
                  new Player("Freddy", "Midfielder"),
                  new Player("Steve", "Center Forward"),
                  new Player("Dale", "Goal Keeper")
                )
              )
            )
          firstTeam.getGoalkeeper.get.name must be_==("Dale") }
      }
    }
    
    class Player(val name: String, val position: String)
    
    class FootballTeam(val teamName: String, val year: Int, val players: Option[List[Player]]) {
      def getGoalkeeper: Option[Player] = {
        players.getOrElse(List.empty[Player]).find(_.position == "Goal Keeper")
      }
    }
    ```
* Unit Specification 은 org.spec2.mutable.Specification을 확장합니다.
* 예제에서 Specs2는 ScalaTest와 다른 Matcher를 가지고 있음을 알 수 있습니다. (must be 대신 must be _== 를 사용)
* 앞서 언급했듯이 Specs2 테스트는 Promise를 사용하여 별도의 스레드에서 서로 독립적으로 비동기 적으로 실행됩니다. 
* Promise는 Actor 모델을 사용하고 개별 스레드에서 실행되는 Scala 프로세스입니다. 이러한 스레드는 개체 (이 경우 ExecutedResult)를 서로에게 보낼 수 있습니다. 이것은 차례로 테스트 결과를 다시 보내는 데 사용됩니다.
    
* specs2 를 활용하는 unit Specification 의 다른 방법
* >> 블록은 중첩 될 수 있으며 가장 바깥 쪽 블록은 광범위한 스펙트럼 상황을 정의하고 가장 깊은 블록은보다 정확한 관점을 정의하도록 테스트를 구성 할 수 있습니다.
    ```
    class AnotherTest extends org.specs2.mutable.Specification {
      "this is another specification" >> {
        "where first example must be true" >> {
          "Hello" must_== "Hello"
        }
    
        "where second specification must be false" >> {
          "World" must_!= "Earth"
        }
      }
    }
    ```
  
## * Acceptance specification (기능 명세)
* Unit Specification 과 달리 여기에서는 org.spec2.mutable.Specification 대신 org.spec2.Specification을 가져옵니다.
    ```
    class ExampleAcceptanceSpec extends Specification { def is =
      "Our example specification" ^
        "and we should run t1 here" ! t1 ^
        "and we should run t2 here" ! t2
      def t1 = success
      def t2 = pending
    }
    ```
* 이 예에서 is 메서드는 전체 테스트를 구현하는 메서드입니다. 모든 예제가 포함된 Fragments 들이 이 메서드에서 반환됩니다.
이 기술은 모든 케이스를 포함하는 Fragments 객체를 반환합니다. 
* ExampleAcceptanceSpec에는 두 가지 개별 전략이 있습니다. 

* 첫 번째는 프라이머 문자열 뒤에 지정된대로 접근 방식 t1을 실행합니다. 그런 다음 두 번째 소개 문자열 뒤에 t2를 실행할 전략으로 지정합니다. 
* ! Operator 를 테스트를 개별 스레드에서 실행할 수있는 블록으로 분리하기위해 사용합니다.
* ^(Carets)는 테스트를 분리합니다. 
* !를 사용하지 않는 모든 문자열 메서드를 호출하는 연산자는 다음 테스트의 헤더로 표시됩니다. 
* 앞의 예에서는 ^가 뒤에 오는 단순 스펙입니다.
* Scala Actor 모델은 Specs2 테스트를 실행하는데 사용됨

* 병렬로 테스트 하는 기능을 끄고 순차적으로 테스트 하려면... 아래처럼...
    ```
    class ExampleSequentialAcceptanceSpec extends Specification { def is =
      args(sequential = true) ^
        "This is an example specification" ^
        "and this should run t1"          ! t1 ^
        "and this example should run t2"  ! t2
      def t1 = success
      def t2 = pending
    }
  
    [info] Compiling 1 Scala source to /home/packt/scala_tdd_book.git /chapter8/target/scala-2.9.2/test-classes...
    [info] [info] [info] [info] [info]
    Our example specification
    + and we should run t1 here
    * and we should run t2 here PENDING
    Total for specification ExampleAcceptanceSpec [success] Total time: 5 s
    ```
  
* 앞의 결과에서 +는 t1 이 성공적으로 실행되었음을 나타냅니다.
* PENDING 옆에 표시된 마지막 테스트 결과에는 테스트가 보류 중임을 나타내는 * 기호가 있습니다. 

* 아래 예제인 acceptance 테스트에는 세 가지 Specification 가 있습니다. 세 가지 모두 현재 보류 중입니다.
    ```
    class EmployeeAcceptanceSpec extends Specification { def is =
      "An employee should have a middle name at construction" ^
        """An employee should be able to be constructed with a middle
           name and get it back calling 'middleName'""" ! makeAnEmployeeWithMiddleName ^
        """An employee should be able to have a full name made of the first
           and last name
           given a first and last name at construction time""" ! testFullNameWithFirstAndLast ^
        """An employee should be able to have a full name made of the first,
           middle and last name
           given a first, middle, and last name at construction time""" ! testFullNameWithFirstMiddleAndLast
      def makeAnEmployeeWithMiddleName = pending
      def testFullNameWithFirstAndLast = pending
      def testFullNameWithFirstMiddleAndLast = pending
    }
    ```
* 이 시점에서 보류중인 Specification 을 몇 가지 유용한 테스트로 대체합니다.
    ```
    class EmployeeAcceptanceSpec extends Specification { def is =
      "An employee should have a middle name" ^
        """An employee should be able to be constructed with a Option[String] middle
           name and we should be able to get it back calling 'middleName'""" ! makeAnEmployeeWithMiddleName ^
        """An employee should be able to have a full name made of the first
           and last name
           given a first and last name at construction time""" ! testFullNameWithFirstAndLast ^
      """An employee should be able to have a full name made of the first,
         middle and last name
         where first, middle, and last name at construction time""".stripMargin ! testFullNameWithFirstMiddleAndLast
    
      def makeAnEmployeeWithMiddleName = {
        val gaurav = new Employee("Gaurav", "Maken", "Sood")
        gaurav.middleName must be_==(Some("Maken"))
      }
    
      def testFullNameWithFirstAndLast = {
        val khush = new Employee("Khushboo", "Sood")
        khush.fullName should be_==("Khushboo Sood")
      }
    
      def testFullNameWithFirstMiddleAndLast = {
        val kids = new Employee("Johan", "And", "Johan")
        kids.fullName should be_==("Johan And Johan")
      }
    }
    ```
    
## * Matchers
* ScalaTest와 마찬가지로 Specs2의 Matchers는 기대치에 대한 결과를 확인하는 목적으로 사용됩니다.
* 테스트 결과는 일부 예상 값과 비교되며 이는 Matchers를 사용하여 수행됩니다.
* 이것은 고전적인 Arrange-Act-Assert 의 원형입니다. (AAA)
  * Arrange – 테스트 개체를 설정하고 테스트를위한 전제 조건을 준비합니다.
  * Act – 테스트의 실제 작업을 수행합니다.
  * Assert – 결과를 확인합니다.
* 가장 간단한 예는 직원의 전체 이름을 반환하는 개체에 대한 사양입니다.
    ```
    // describe the functionality
    s2"the getFullName method should return full name of the employee $e1"
  
    // give an example with some code
    def e1 = Employee.getFullName() must beEqualTo("Khushboo Maken Sood")
    ```
* 여기서 must 연산자는 getFullName 메서드에서 반환 된 결과를 가져와 Matcher (이 경우 beEqualTo)에 전달합니다.
* 다양한 유형의 Matchers를 더 자세히 살펴 보겠습니다. (PDF 로...)

## * Specs2 data tables
* Specs2의 데이터 테이블은 테이블 기반 속성 검사의 테이블과 유사합니다. 
* 테이블은 샘플 입력 값과 예상 결과로 구성됩니다. 
* 함수 테스트 케이스는 입력이 함수에 대한 입력으로 작동하는 테이블의 각 행에 대해 실행되는 테이블을 따르며 함수의 결과는 테이블의 예상 출력으로 확인됩니다.
* https://etorreborre.github.io/specs2/guide/SPECS2-4.10.0/org.specs2.guide.UseDatatables.html

## * 요약
* Specs2는 Scala 애플리케이션 코드의 단위 테스트를위한 ScalaTest와 마찬가지로 인기 있고 강력한 테스트 프레임 워크입니다. 
* Specs2 사용에는 장단점이 있습니다. Matcher의 광범위한 선택 및 병렬 테스트 실행과 같은 몇 가지 이점이 있습니다. 
* Specs2 또는 ScalaTest를 사용할지 여부는 개별점인 선호에 따른다.