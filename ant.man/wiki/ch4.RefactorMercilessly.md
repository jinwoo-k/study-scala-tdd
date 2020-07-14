# 4장 무자비하게 리펙토링
- Clean code
- Red-Green-Refactor
- Code smell
- To refactor or not to refactor?
- Refactoring techniques
- Building reusable test code

## 4.1 Clean code
- 간단 (트릭없고, 줄임없고, 이해쉬움)
- 모든 테스트 통과하고 중복 없음

## 4.2 Red-Green-Refactor
- 애자일 (+ XP)
- 리펙토링 안하면 기술적 부채가 쌓이는 것과 같음
- 2장에서 한것처럼 테스트 중심 사이클(red-green-refactor) 의 마지막 단계
  - 돌아가는 코드 (테스트 통과) 내에서 깨끗하게 만들기

## 4.3 Code smell
코드 표면에 냄새가 난다는 표현-> 쉽게 확인하는 방법은 테스트를 쉽게 할 수 있느냐의 척도
- Expendable (소모품 -> 제거)
  - 주석, 중복, 안중요한클래스, 죽은코드, 일어나지않을 가설 코드
- Couplers (과도한 연결)
  - 메소드에서 과도하게 클래스사용, 다른 클래스의 내부필드사용, 여러함수 채이닝, 클래스 기능 다른 클래스에 모두 위임
- Modification thwarters
  - 관련없는 걸 변경해야할때, 조금 변화를 위해 많이 수정될때, 하위 클래스 만들때 1개이상 다른 클래스 생성
- Bloaters (작업하기 어려울 정도 큰 메소드나 클래스)
  - 큰 메소드 (10줄)/클래스(200줄), Primitive 집착, 긴 파라미터, 데이터 덩어리
- Object-oriented abusers (모든 냄새는 객체지향을 제대로 안써서 생김)
  - 스위치문, 임시필드, 거부된 요청(subclass에서 기능 못쓸때),인터페이스가 다른 대체클래스 (중복기능)
- Obsolete libraries (사용되지 않는 라이브러리)
  - '바퀴를 재발명하지 말라' 하지만 사용하지 않거나 업데이트 안되는 라이브러리는 수정할것.

## 4.4 To refactor or not to refactor?
- Doing it thrice (rule of three)
- Adding new feature
- Bug fixing
- Code reviews


## 4.5 Refactoring techniques
- Composing methods
  - extract method vs Inline method
  - Extract variable (expression that is hard to understand) vs Inline temp
- Moving features between objects
  - move method/ field
  - Extract/inline class
  - Hide delegate vs Remove middle man
  - Introduce foreign method
  - Introduce local extensions (유틸 클래스를 확장하거나 새 클래스에 기능 다 넣기)
- Organizing data
  - Self-encapsulate field (getter/setter사용)
  - Replace data value with object
  - Change value to reference vs Change reference to value
  - Replace array with object
  - Duplicate observed data (데이터와 뷰 분리)
  - Change unidirectional association to bidirectional vs bidirectional relationship to unidirectional
  - Replace magic number with symbolic constant
  - Replace type code with a class (Enum 사용)



## summary
- 리펙토링: 외부동작을 바꾸지 않으면서 내부 구조를 개선하는 방법
- 소프트웨어를 보다 이해하기 쉽고, 수정하기 쉽도록 만드는 것,  겉으로 보이는 소프트웨어의 기능을 변경하지 않는 것 (한번에 한 작업만 할것)

<추가>
https://www.slideshare.net/ddayinhwang9/refactoring-60121460
