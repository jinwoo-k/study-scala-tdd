# 6장 mock 객체와 stubs

## Couplings
- 서로 다른 모듈의 상호 의존성
  - tight coupling and loose coupling (한 모듈이 바뀌는것에 따라 다른 모듈이 바뀌는지가 척도)
  - 커플 링은 코드의 테스트 가능성에서 중요한 역할을 함. 코드가 느슨하게 결합될수록 더 테스트 가능
  - 모듈을 독립적으로 테스트하는 것이 더 쉬움

## Stubs
- stub: 미리 입력된 데이터로 미리 정의된 결과를 생성하는 객체
  - 사용법이 매우 제한적

## Mocks
- mock: stub과 같이 미리 설정된 값을 반환, mock 호출횟수와 호출순서에 대한 기대치도 프로그래밍 가능
  - 모의 객체를 사용하면 실제 클래스를 사용하지 않고 클래스 간의 상호 작용에 대한 특정 세부 사항을 테스트하는 테스트가 수행

-  Mock Object는 행위 검증(behavior verification)에 사용하고, Stub은 상태 검증(state verification)에 사용하는 것
  - 참고: https://martinfowler.com/articles/mocksArentStubs.html
  - https://medium.com/@SlackBeck/mock-object%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80-85159754b2ac

- Expectations
  - 기대치를 설정해 동작을 정의, 테스트가 예상한대로 동작하는지 확인 (횟수에 대한 충족 테스트는 못함)
    - 응답, 에러, 순서만 검증
- verification
  - 기대치를 충족하는지 설정

## Fakes
- Fake object : 인터페이스나 trait 의 실제 구현체, 테스트에 한정되서 사용되는 객체

## Spy
- 로거 처럼 행동함. 유닛테스트의 assert 후 spy로부터 로그를 확인

## Mocking frameworks
- JMock
  - java 중심의 테스트 코드
  - mock objects and stubs 지원
  - 장점
    - 자바에서 스칼라로 쉽게 전환
    - 설치 및 사용이 매우 쉬움
    - 주석 지원이 덜 눈에 띄게 됨
    - 객체 간의 정확한 상호 작용을 정의하는 기능
    - 대부분의 새 IDE에는 JMock에 대한 지원 / 플러그인이 있으며 자동 완성을 지원하는 코드를 지원
    - ScalaTest 또는 Specs2에 쉽게 연결
    - 확장 가능
  - http://www.jmock.org/
```scala
val cycle = new JMockCycle
import cycle._
val mockCollaborator = mock[Collaborator]
expecting { e => import e._
  oneOf (mockCollaborator).addToTotal(200)
exactly(3).of (mockCollaborator).addedToTotal(200)
}
whenExecuting {
  classUnderTest.addToTotal(200)
  classUnderTest.addToTotal(200)
  classUnderTest.addToTotal(200)
}
```
- EasyMock
  - 이름에서 알듯 mock 을 쉽게 만듦
  - http://easymock.org/
```scala
val mockCollaborator = mock[Collaborator]
expecting {
  mockCollaborator.documentAdded("Document")
  mockCollaborator.documentChanged("Document")
  lastCall.times(3)
}
whenExecuting(mockCollaborator) {
  classUnderTest.addDocument("Document", new Array[Byte](0))
  classUnderTest.addDocument("Document", new Array[Byte](0))
  classUnderTest.addDocument("Document", new Array[Byte](0))
  classUnderTest.addDocument("Document", new Array[Byte](0))
}
```

- Mockito
  - 모든 프레임워크 중 제일 쉬움
```scala
// First, create the mock object
val mockService = mock[ServiceClass]
// Create the class under test and pass the mock to it
classUnderTest = new ClassUnderTest
classUnderTest.addService(mock)
// Use the class under test
classUnderTest.createPayroll("Person1", 1000)
classUnderTest.createPayroll("Person1", 4000)
classUnderTest.createPayroll("Person1", 6000)
classUnderTest.createPayroll("Person1", 19000)
// Then verify the class under test used the
// mock object as expected
verify(mockCollaborator).createInitalPayroll("Person1")
verify(mockCollaborator, times(3)).changePayroll("Person1")
```
  - https://www.programcreek.com/scala/org.mockito.Mockito

- ScalaMock
  - 기존 프레임워크들과 다르게 java base 가 아님. 함수형과 객체지향이 모두 반영됨
  - 장점
    - 안전한 타입
    - 오픈 소스
    - 다음과 같은 스칼라 관련 기능 지원 : Operator methods, 메소드 오버로딩, 패턴 매칭, Parameterized method
    - ScalaTest 및 Specs2와 호환
  - mockFunction: 함수 mock
  - Proxy mocks: interface 나 trait 을 mock
  - Generated mocks
    - mock.annotation(클래스 생성), mockWithCompanion (동반객체), mockObject(싱글톤)
  - https://scalamock.org/quick-start/
  - 추가 장점
  ```scala
// Repeated parameters can be mocked in expectations using **:
m expects 'takesRepeatedParameter withArgs
(42, **("red", "green", "blue"))
// Mocks can also be set to return values:
calc expects (2, 4) returning 6
// Mocks can also be set to throw exceptions:
calc expects (0, 2) throws new AirthmeticException("Divide by zero")
  ```


## summary
- 개발자와 mock 객체는 애증의 관계
- class를 독립적으로 테스트 할 수 있다는 이점 -> 느슨하게 결합 된 아키텍처로 발전하며 코드 재사용성 높임
- mock는 테스트 코드를 테스트중인 클래스의 실제 구현에 연결
- 테스트 코드에 주요 리팩토링이 수행되면 테스트에서 모의 예상 및 검증이 변경 될 수 있음. 이것은 테스트 및 응용 프로그램 코드의 분리 원칙에서 벗어남.
  - 필요한 곳에서만 쓰고, mock 은 좀 멀리서 사용하라..
