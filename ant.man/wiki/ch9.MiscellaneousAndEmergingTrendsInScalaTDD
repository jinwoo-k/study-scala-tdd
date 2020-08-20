# 9장 스칼라 TDD 에서 떠오르는 여러 트랜드
- 완전 최고의 기술이나 프로세스는 없음 -> 항상 개선되고 더 나은 것만이 있음
- 향후 유용할 만한 몇가지 살펴봄
  - Scala Futures and Promises
  - Inside trait
  - Option values
  - Either values
  - Eventually and integration patience
  - Consumer-Driven Contracts


## 9.1 Scala Futures and Promises
- 스칼라 Futures 는 java Futures 의 확장
  - java 에서는 (java.util.concurrent.Future) blocking 없이는 값 없을 수 없음
    - 자바 8에서는 람다의 추가와 함께 CompletableFuture가 추가되었다. 자바의 CompletableFuture는 Scala의 Future와 비슷하다. 먼저 언젠가 얻게 될 값을 정의한 후, 그 값을 얻었을 때 하는 행동을 정의
  - scala 에서는 완료(success / failure)를 첨부하거나 map 으로 처리, 여러 Futures를 monadic으로 chain (완료 여부와 관계없이 변환을 지정)

- java와 같이 future 는 non-blocking asynchronous task의 결과

- Futures and Promises 의 기본동작은 non-blocking 이고 callbacks (비동기 작업의 결과를 얻기위한) 제공
  - Scala에서는 flatMap, filter 및 foreach와 같은 메서드를 사용하여 Futures를 non-blocking 사용

- 아래 두 예제 모두 non-blocking 실행은 ExecutionContext에 위임. 결과는 studentFuture
```
// 첫번째 방법
Val studentFuture:Future[StudentDetails] {
  // non-blocking rest call or computation
  studentService.studentDetails('Student 101')
} (executionContext)
// 두번째 방법
Implicit val ec:ExecutionContext = ...
Val studentFuture:Future[StudentDetails] {
  // non-blocking rest call or computation
  studentService.studentDetails('Student 101')
} //ec is passed implicitly
```

### 9.1.1 ExecutionContex
- ExecutionContext는 non-blocking 를 실행
  - 자바에 Executor는 ExecutionContext와 Executor사이에 parallel 가능
    - ExecutionContext와 같이 Executor는 별도의 스레드서 작업 실행함
  - 스칼라에서는 전역 execution context 는 ForkJoinPool를 서포트 (이건 자바 ExecutorService의 구현체)
    - ForkJoinPool 프레임 워크가 있으므로 멀티 프로세서의 이점을 누림
    - 큰 업무를 작은 업무로 나누어 배분해서, 일을 한 후에 일을 취합하는 형태
    - ForkJoinPool은 제한된 수의 스레드를 관리 (이 수는 병렬 처리 수준이라고도하며 프로세서 수에 따라 다름)
    - ExecutionContext.global은 별도로 지정하지 않는 한 병렬 처리 수준을 프로세서 수로 기본값으로 설정

### 9.1.2 Futures
- future는 아직 사용 불가. ExecutionContext에 의해 실행되는 별도의 non-blocking 태스크에서 계산되는 값을 보유하는 객체
- future 작업이 완료 후 결과값을 사용할 수 있는 경우 "completed" 반대로 아직 안끝나면 "not completed."
- future 가 보유할 값은 한번만 할당. (값이나 예외를 받으면 immutable 됨)

```scala
import scala.concurrent._
import ExecutionContext.Implicits.global
val session = EmployeeService.session
val employeeFuture: Future[List[Employee]] = Future {
  session.getAllEmployees()
}
```

- 성공적으로 완료되면이 콜백이 Success [T] 유형의 값, 예외가 발생하면 Failure [T] 유형
```
import scala.util.{Success, Failure}
import scala.concurrent._
import ExecutionContext.Implicits.global
val session = EmployeeService.session
val employeeFuture: Future[List[Employee]] = Future {
  session.getAllEmployees()
}
employeeFuture onComplete {
  case Success(employees) => for (employee <- employees)
    println(employee.getFullName)
  case Failure(t) => println("An error has occured: " + t.getMessage)
}
```
- onComplete 대신, onSuccess 와 onFailure callback 사용
```
employeeFuture onFailure {
  case t => println("An error has occured: " + t.getMessage)
}
employeeFuture onSuccess {
  case Success(employees) => for (employee <- employees)
  println(employee.getFullName)
}
```

## 9.2 The Inside trait
- scalatest 에 inside trait 이 있음
  - https://www.scalatest.org/user_guide/using_inside

```
case class Address(street: String, city: String, state: String, zip: String)
case class Name(first: String, middle: String, last: String)
case class Record(name: Name, address: Address, age: Int)
```
```
inside (rec) { case Record(name, address, age) =>
  inside (name) { case Name(first, middle, last) =>
    first should be ("Sally")
    middle should be ("Ann")
    last should be ("Jones")
  }
  inside (address) { case Address(street, city, state, zip) =>
    street should startWith ("25")
    city should endWith ("Angeles")
    state should equal ("CA")
    zip should be ("12345")
  }
  age should be < 99
}
```
```
val rec = Record(
  Name("Sally", "Anna", "Jones"),
  Address("25 Main St", "Los Angeles", "CA", "12345"),
  38
)
/* error console
An exception or error caused a run to abort: "Ann[a]" was not equal to "Ann[]",
  inside Name(Sally,Anna,Jones),
inside Record(Name(Sally,Anna,Jones),Address(25 Main St,Los Angeles,CA,12345),38)  */
```
```
val rec = Record(
  Name("Sally", "Ann", "Jones"),
  Address("25 Main St", "Los Angeles", "CA", "12345"),
  100
)
/* error console
An exception or error caused a run to abort: 100 was not less than 99,
inside Record(Name(Sally,Ann,Jones),Address(25 Main St,Los Angeles,CA,12345),100) */
```

## 9.3 Option values
- https://www.scalatest.org/user_guide/using_OptionValues

```
/// 1
val opt: Option[Int] = None
opt should be ('defined) // throws TestFailedException
opt.get should be > 9

/* error console
An exception or error caused a run to abort: None was not defined
org.scalatest.exceptions.TestFailedException: None was not defined */

/// 2
val opt: Option[Int] = None
opt should be ('defined) // throws TestFailedException
opt.get should be > 9

/// 3
val opt: Option[Int] = None
opt.value should be > 9 // opt.value throws TestFailedException
```

## 9.4 Either values
- https://www.scalatest.org/user_guide/using_EitherValues

```
//1
val either: Either[String, Int] = Left("Muchas problemas")
either.right.get should be > 9 // either.right.get throws NoSuchElementException

// 2
val either: Either[String, Int] = Left("Muchas problemas")
either should be ('right) // throws TestFailedException
either.right.get should be > 9
```

## 9.5 Eventually and integration patience

- 함수를 반복적으로 실행. 전달된 함수가 성공하거나 타임아웃될때까지 수행
  - 이름으로 전달된 함수는 반환할때 "성공"으로 간주, 함수서 예외발생하면 테스트 실패

```scala
class EventuallyTraitTest extends UnitSpec with Eventually {
  val alphabets = 'a' to 'z'
  val iterator = alphabets.iterator
  eventually { iterator.next should be ('c') }
  // eventually { Thread.sleep(50); iterator.next should be ('c') } // test fail. default timeout : 150 milliseconds
}
```
- 두 매개변수
  - timeout: 기본값 150 milliseconds, maximum time in unsuccessful attempts
  - interval: 기본값 15 milliseconds, sleep interval between each iteration/attempt.
- 암시적 메서드 : PatienceConfig 객체

```scala
implicit override val patienceConfig =
    PatienceConfig(timeout = scaled(Span(5, Seconds)), interval = scaled(Span(3, Millis)))

// Change timeout:
eventually (timeout(Span(2, Seconds))) { iterator.next should be ('p') }

// Change interval:
eventually (interval(Span(3, Millis))) { iterator.next should be ('p') }

// Change both:
eventually (timeout(Span(2, Seconds)), interval(Span(3, Millis))) { iterator.next should be ('p') }
```
- Simple backoff algorithm
  - 내부적으론 eventually가 비동기이므로 interval 잘 조절 (실제 수행속도보다 interval 빠를수 있음)
  - 초기 interval 은 전체의 1/10만 수면
- Integration patience
  - https://javadoc.io/static/org.scalatest/scalatest_2.12/3.0.8/org/scalatest/concurrent/IntegrationPatience.html
  - 단위 테스트에 기본 시간제한을 함께 사용하도록 구성 (또는 자신이 만든 trait 으로 timeout, interval 조절해도 좋음)
    - IntegrationPatience 트레이 트는 기본 제한 시간을 150 밀리 초에서 15 초로 늘리도록 구성됩니다.
    - 간격의 기본값을 15 밀리 초에서 150 밀리 초로 늘리도록 구성되어 있습니다.
```
class ExampleSpec extends FeatureSpec with Eventually with IntegrationPatience {
  // Your integration tests here...
}
```

## 9.6 Consumer-Driven Contracts
- 서로 다른 모듈 간의 통신을 결정하는 몇 가지 패턴. 마이크로 서비스 아키텍처에서 특히 유용

- Consumer-Driven Contracts: 서비스 소비자가 상호작용의 종류/형식을 지정하는게 소비자 책임

- 특징
  - 독립적이고 철저. 즉, 기존 소비자가 요구하는 모든 기능을 포함
  - 비범하고 신뢰할수 없음. 공급자의 계약은 기존 소비자의 기대를 합친 결과이기때문에 고정 안됨.
  - 제한된 항상 및 불변성. 계속 안바뀌어야 함. 소비자 계약에 따라 유효성을 확인 할 수 있음

- interface 서비스 하는 두가지 방법
  - gigantic, 일반적인 모놀리식 application (한 jvm 내), 이 경우 통합 테스트로 문제 확인 가능
  - Microservices, 공급자의 변경이 컴파일러에 의해 확인 불가
    - 주요 변경 사항 있을 시 기존껄 두고 추가해야 함, api변경 시 바로 확인 불가

- CDC를 사용해 microservices 통합
  - 전통적인 방법
    - 두 서비스간 호환성 테스트는 두 서비스를 모두 시작하고 예상대로 통신되는지 확인
    - 문제는 한 서비스가 다른서비스를 제어 못함 (소비자는 공급자의 변경을 확인후 공급자의 변경에 따라야함)
  - CDC 사용
    - 두 통신이 이뤄지는 중간에서 통합 테스트 작업을 분할
    1. 소비자는 특정 요청에서 공급자에게 필요한걸 지시
    2. 공급자와 소비자 모두이 서비스 계약에 동의
    3. 공급자는 항상 계약을 충족하는지 확인하기 위해 통합 테스트를 수행
    4. 이건 전통적인것과 다른 몇가지 변경을 가짐
      - 소비자가 공급자에게 필요한 것을 정의 할 수있는 방법이 필요 (+계약을 게시할 방법)
      - 공급자기 읽고 검증할 방법
      - 양 당사자가 서로의 상태 확인

![이미지 이름](https://martinfowler.com/articles/consumerDrivenContracts/ConsumerDrivenContracts.jpg)

- 장점
  - 서비스는 구현을 위해 서로 의존하지 않음. 통합테스트를 위해 다른 서비스를 실행 안해도 됨 (공급자가 계약을 확인하는 한 서비스는 함께 작동)
  - 서비스 소비자는 계약의 성립 및 지속에 대한 책임 (서비스의 실행 가능성에 대한 책임을 공급자에서 소비자로)
  - 공급자의 애플리케이션 코드는 변경에 더 탄력적 (공급자가 응용 프로그램 코드를 변경할 때 해당 서비스가 계약을 준수하는지 확인하기만)

## 출처
- future 관련
  - https://hamait.tistory.com/763
  - https://partnerjun.tistory.com/26
  - https://stackoverflow.com/questions/31366277/what-are-the-differences-between-a-scala-future-and-a-java-future
  - 책에서 참고: https://danielwestheide.com/blog/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice/
  - promise 와 future : https://yehongj.tistory.com/56
    - Future가 non-blocking 방식의 callback 부분을 monad 방식으로 추상화 시킨 것이라면,
    - Promise는 단순히 코드 구조를 편하게 작성하게 하기 위한 것이다. 즉 Future는 물리적인 의미가 있지만, Promise는 Future를 좀 더 편하게 코딩할 수있게 하는 단순 라이브러리
- blocking non-blocking, synchronous, asynchronous
  - https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/
  - https://brainbackdoor.tistory.com/26
- 쓰레드풀 과 ForkJoinPool:  https://hamait.tistory.com/612
- consumer-driven-contracts
  - https://reflectoring.io/7-reasons-for-consumer-driven-contracts/
  - https://martinfowler.com/articles/consumerDrivenContracts.html
