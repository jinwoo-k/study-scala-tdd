# 8장 Specs2 와 함께 스칼라 테스트
- specs2 는 실행 가능한 사양 작상이 편한 유명한 라이브러리임

## Differences between Specs2 and ScalaTest
- 전체 테스트 구조 다름, syntax 와 matcher 다름
- specs2 는 BDD 에 전념, scalatest 는 더 일반적
- specs2는 더 많은 matcher 가짐 (예를들어 그 파일은 디렉토리)
- scalatest의 기본 5개 연산자 (===, > , <, >=, <=) 보다 많은 operator (->-, >> , | , |>, ! , ^^^)
- specs2는 data tables 를 가짐
- specs2는 별도의 스레드에서 동시 실행가능

## style
- scala code를 한곳에 모으고 다른곳에 text 를 작성
  - scala 코드와 텍스트 상호배치 가능

- ExampleAcceptanceSpec에는 두 가지 개별 전략이 있습니다.
- 첫 번째는 프라이머 문자열 뒤에 지정된대로 접근 방식 t1을 실행합니다. 그런 다음 두 번째 소개 문자열 뒤에 t2를 실행할 전략으로 지정합니다.
- ! Operator 를 테스트를 개별 스레드에서 실행할 수있는 블록으로 분리하기위해 사용합니다.
- ^(Carets)는 테스트를 분리합니다.

## matcher
- 이것은 고전적인 Arrange-Act-Assert 의 원형입니다. (AAA)
  - Arrange – 테스트 개체를 설정하고 테스트를위한 전제 조건을 준비합니다.
  - Act – 테스트의 실제 작업을 수행합니다.
  - Assert – 결과를 확인합니다.

- string
- numeric value
- object ,class, reference
- option/either value
- try monad
- exception
- data structure (map, Iterables, sequence, traversable)
- xml
- file
- partial function
- data tables

## 참고
- https://hamait.tistory.com/629
  - mockito 가 기본 들어가있음
- https://www.scalatest.org/at_a_glance/Spec
  - 여러 matcher 별 예제코드 (책 예제도 포함)
- https://code.google.com/archive/p/specs/wikis/MatchersGuide.wiki
  - 여러 matcher 코드
- https://etorreborre.github.io/specs2/guide/SPECS2-2.4.17/org.specs2.guide.Matchers.html#Matchers
