# Chapter4. Refactor Mercilessly
코드 리팩토링은 지속적인 프로세스다. 이번 장에서는 아래 내용을 다룬다.

* 클린 코드
* 레드 그린 리팩토링
* 코드 스멜
* 리팩토링 하는 것 과 그렇지 않은 것
* 리팩토링 기술
* 재사용 가능한 테스트 코드 작성

## Clean code
클린 코드는 간단한 코드이다. 속임수와 지름길이 없고 이해하기 쉽다.모든 테스트를 통과해야하며 중복이 없어야 한다.

## Red-Green-Refactor
무자비한 리팩토링(refactor mercilessly)은 XP의 일부로 만들어졌으며 매우 짧은 반복, 연속 릴리즈 및 페어 프로그래밍을 포함하는 몇가지 인기있는 애자일 프로세스 중 하나이다.

스토리 추정시 리팩토링에 대한 프로세스를 항상 추가하는게 좋다. 리펙토링은 녹색 이후에 바로 진행되며 리팩토링 과정에서 지속적으로 녹색이 유지된다면 기능이 정상적으로 작동함을 보장한다. 테스트가 지속적으로 통과되는 범위 내에서 무자비하게 리팩토링 할 수 있다. 

리팩토링은 애플리케이션 코드에만 국한되지 않고 테스트 코드도 리팩토링 해야 한다. 재사용성과 클린 코드는 리팩토링 노력의 결과이다.

## Code smell
코드 스멜은 더 깊은 문제를 암시하는 코드 표면의 표시이다. 코드의 테스트 가능성이 가장 큰 코드 스멜이다. 코드를 간단한 테스트로 테스트할 수 있다면 코드가 타이트하게 커플링되지 않았다는 의미이다. 또한 코드가 하나의 작업만 수행한다는 의미이며 이러한 것은 엔드 투 엔드로 테스트 할 수 있다.


### Expendable (소모적인, 소비해도 되는)
주석 : 프로그래머로서 우리는 때때로 가능한 가장 비밀스러운 방식으로 일을 한 다음 코드 주위에 주석을 작성하여 어떤 독창적 인 해결책을 제시했는지 설명한다. 이것은 주석 주위에 짜여진 코드로 이어진다. 의도는 코드 자체로 드러나야 하고 테스트 코드가 문서 역할을 하게 해야 한다. 이를 위해서는 코드는 간단하게 이해할 수 있어야 한다.

코드 중복 : 코드 중복은 코드 스멜과 모든 악의 근원이다. 애플리케이션 코드베이스에 동일한 코드가 여러 번 작성되는 것이 보인다면 문제가 있는 것이다. 중복 코드가 구문에서 동일한 코드임을 의미하지는 않는다. 기능의 중복 일 수도 있다. 예를 들어, 두 클래스가 서로 다른 두 가지 방법으로 동일한 데이터베이스에 대한 연결을 작성될 수 있다.

중요하지 않은 클래스 : 거의 수행하지 않거나 매우 중요하지 않은 클래스는 코드 스멜이다. 대부분의 경우 인라인 클래스로 대체할 수 있다. 

도달 할 수 없고 죽은 코드 : 리팩토링의 잔여 물일 수 있다. 새로운 IDE의 대부분은 도달 할 수없는 코드를 식별하는 데 능숙하다. 데드 코드는 더 이상 필요하지 않은 변수, 필드, 메서드 또는 클래스 일 수 있다.

가정의 구현 : 때때로 우리는 구현의 기능에 관여하여 구현을 미래에 대비하는 코드를 작성한다. 이것은 우리가 결코 일어나지 않을 것으로 예상되는 미래의 변화를 지원 하기 위한 코드를 작성한다는 것을 의미한다. YAGNI (You Ain't Gonna Need It)의 약어이다.


### Couplers
이 코드 스멜은 클래스 간 과도한 커플링으로 나온다. 이로 인해 클래스를 독립적으로 테스트 할 수 없다.

Feature envy : 메소드가 다른 클래스를 광범위하게 사용하는 경우이 메소드가 다른 클래스에 속해야 함을 의미

부적절한 연결 : 클래스가 다른 클래스의 내부 필드와 메서드를 사용하는 경우 이것은 캡슐화 규칙을 위반

함수 체이닝 : a.b().c().d()와 같이 여러 함수가 서로 체인으로 연결되어 있으면 클라이언트가 클래스 구조를 따라 탐색하여 냄새가 난다는 것을 의미

브로커 클래스 : 클래스가 기능을 다른 클래스에 위임하도록 설계된 경우이 클래스가 전혀 필요하지 않을 수 있다.

### Modification thwarters (수정 철벽자)

수정사항 이탈 (Deviating modification) : 클래스를 변경할 때 관련없는 많은 메소드를 변경하는 경우. 이것은 대부분 "복사-붙여 넣기 프로그래밍"의 결과이다.

산탄총 수술(Shotgun surgery:) : 하나의 작은 변경을 수행하려면 여러 클래스를 변경해야한다. 이는 여러 클래스로 책임이 분할 되었음을 의미합니다. - high cohesion, loose coupling 원칙을 위배한것

병렬 상속 계층 구조 : 클래스의 하위 클래스를 만들 때마다 하나 이상의 다른 클래스의 하위 클래스도 만들어야한다.


### Bloaters (훈제청어..?)
작업하기 매우 어렵게 큰 클래스나 메서드

Long method : 경험상 일반적으로 10 줄 이상의 코드는 코드 스멜이다.

Large class : 많은 필드와 메소드를 포함하는 클래스입니다. 이상적으로 클래스는 최대 약 200 줄이어 한다.

프리미티브 강박관념 : 코드가 정보 또는 필드명으로 작은 객체 나 상수 대신 프리미티브 형식을 사용하는 경우. 다른 많은 코드 스멜과 마찬가지로 일부 데이터를 저장하기 위해 필드가 필요할 때 약점이 드러난다.

Long parameter list : 분석법에 3 개 이상의 매개 변수가 필요한 경우. 이는 여러 알고리즘이 단일 방법으로 병합되었음을 의미한다.

데이터 덩어리 : 때로는 코드의 다른 부분에 데이터베이스에 연결할 때와 같이 동일한 변수 그룹이 포함되는 경우가 있다. 이들은 자신의 클래스로 옮길 후보입니다.


### Object-oriented abusers
이 스멜은 개체 지향 원칙을 부적절하거나 불완전하게 사용하여 발생한다.

스위치 문(Switch statement) : 복잡한 스위치 문이 있거나 매우 긴 중첩 if문이 있는 경우 다형성 솔루션으로 코드를 다시 작성해야한다.

임시 필드 : 임시 필드는 특정 상황에서만 값을 가져옵니다. 이러한 상황이 발생하지 않으면이 필드는 비어 있습니다. 이 필드를 사용하면 코드를 이해하기가 매우 어렵습니다.

거부 된 요청(Refused bequest) : 서브 클래스에 의해 상속 된 기능의 일부 또는 대부분이 사용되지 않으면 클래스(부모와 자식)는 서로 관련이 없음을 의미합니다. 이러한 경우 구성 또는 위임과 같은 다른 접근 방식에서는 상속을 제거해야 한다.

인터페이스가 다른 대체 클래스 : 두 클래스가 동일한 기능을 수행하지만 메소드 이름이 다른 경우 생각보다 자주 발생합니다. 이는 대부분의 개발자들이이 기능을 제공하고 새로운 클래스를 작성하기 위해 클래스가 이미 존재하는지 알지 못하는 팀 간의 의사 소통이 좋지 않은 결과이다.


### Obsolete libraries (구닥다리 라이브러리들)
때로는 사용중인 서드파티 라이브러리가 자주 업데이트되지 않고 일부 측면에서 더 이상 사용되지 않는다. 때론 자신의 라이브러리를 작성하고 싶을지도 모른다. 그리고 그것은 바퀴를 재발명 할 때 코드 냄새가 난다. 이러한 경우 라이브러리 코드는 읽기 전용이지만 기능을 확장하여 요구를 충족시키는 데 사용할 수 있다.

## To refactor or not to refactor

### Doing it thrice (rule of three)
동일한 일을 세번 반복하면 리팩토링이 필요하다는 신호다.

### Adding new feature
다른 사람이 작성한 불분명한 코드에 새 기능을 추가할 때는 리팩토링을 선행하는게 좋다. 리팩토링은 불분명한 코드를 이해하는데 도움이 되고 원환한 신규 기능 추가를 돕는다.

### Bug fixing
버그는 보통 냄새 나는 코드에 산다. 리팩토링을 시작하면 대부분의 버그가 공개되기 시작한다.

### Code reviews
코드 리뷰 과정에서 코드 냄새가 드러나면 리팩토링해야한다.

## Refactoring techniques

### Composing methods
앞서 다뤘던 코드 스멜의 대부분은 메서드에서 발생한다. 다음 리팩토링 기술은 코드를 간소화하고 중복 제거에 도움이 된다.

메서드 추출 : 함께 그룹화 하거나 현재 위치에 속하지 않는 코드 조각을 독립적인 메서드로 분리한다.
	```
	Before refactoring:
	
	def printReport() {
		printBanner()
		println("Next line of report")
		println("Another line of report")
	}


	After refactoring:
	
	def printReport() {
		printBanner()
		printRestOfIt()
	}

	def printRestOfIt() {
		println("Next line of report")
		println("Another line of report")
	}

	```

인라인 메소드 : 메소드가 매우 간단한 작업을 다른 메소드에 위임하면 메소드가 복잡해진다. 이는 호출 된 메소드의 코드를 호출 된 메소드에 인라인하여 해결할 수 있다.
	```
	Before refactoring:
	def getPrice() = buyingMoreThan5() ? 100:150
	def buyingMoreThan5() = quantity > 5

	After refactoring:
	def getPrice() = quantity > 5 ? 100:150
	```

변수 추출 : 이해하기 어려운 표현식이 있으면 표현식을 정의하는 이름을 가진 변수로 추출해야한다.
	```
	Before refactoring:
	def calculateGrade(person:Person) = 
		if(person.profile.gender == "Male" && person.report.sumOfScore > 80)
			"A"
		else
			"B"
	
	After refactoring:
	def calculateGrade(person:Person) {
		val isMale = person.profile.gender == "Male"
		val scoreMoreThan80 = person.report.sumOfScore > 80
		if(isMale && scoreMoreThan80)
			"A"
		else
			"B"
	}
	```

임시 인라인 : 변수 추출과 반대로 임시 변수의 표현식이 너무 사소하고 쉬우면 제거하는 것 이 합리적이다.
	```
	Before refactoring:
	def giveFree(person:Person) {
		val isMinor = person.isMinor
		isMinor
	}

	After refactoring:
	def giveFree(person:Person) = person.isMinor
	```

임시 변수를 질의로 바꾼다 : 때로는 표현식의 결과를 담는 임시 변수가 있습니다. 이러한 표현은 메서드로 추출 할 수 있으므로 재사용이 가능하고 원래 메서드의 크기를 줄일 수 있다.

	```
	Before refactoring:
	def discountedAmount() {
		val total = quantity * price
		if(total > 100)
			total * 0.95
		else
			total * 0.98
	}

	After refactoring:
	def discountedAmount() { 
		if(getTotal > 100)
			getTotal * 0.95
		else
			getTotal * 0.98
	}
	def getTotal = quantity * price
	```

메소드를 메소드 오브젝트로 바꾸기 : 메소드로 추출하기 어려운 얽힌 계산으로 긴 메소드를 사용하는 경우 그 메서드를 객체로 추출할 수 있는데, 로컬 변수가 필드가 되고 메서드는 여러개의 메서드로 분할된다.
	```
	Before refactoring:
	def price() {
		val primaryBasePrice
		val secondaryBasePrice
		val tertiaryBasePrice
		// long computation. //...
	}

	After refactoring:
	object Order {
		val primaryBasePrice
		val secondaryBasePrice
		val tertiaryBasePrice
		def priceCalculator() {
			....
		}
		def compute() {
			....
		}
	}
	```

알고리즘 대체 : 기존 알고리즘을 새로운 알고리즘으로 대체하고 싶을 때 알고리즘을 구현하는 방법의 본문을 교체할 수 있다.
	```
	Before refactoring:
	def someMethod(){
		// ....
		.... old algorithm
		...
		//
		doSomething()
	}

	After refactoring:
	def someMethod(){
		// ....
		.... new improved algorithm
		...
		//
		doSomething()
	}
	```

### Moving features between objects
이 리팩토링 기술은 서로 다른 클래스간에 기능을 분배 할 때 사용된다. 

메소드 이동 : 메소드가 다른 클래스에서 더 많이 사용되면 이 메소드를 가장 많이 사용되는 클래스로 이동하는 것이 좋다. 원래 매서드을 모두 삭제하거나 새로 이동 한 메서드에 대한 참조를 유지할 수 있다.

필드 이동 : 필드가 생성 된 클래스가 아닌 다른 클래스에서 필드가 더 많이 사용되는 경우 필드를 가장 많이 사용하는 클래스에서 필드를 이동하고 모든 필드를 이전 필드에서 새 필드로 리디렉션 한다.

클래스 추출 : 클래스에 두 클래스의 기능이 포함 된 경우 첫 번째 클래스에서 이 추가 기능을 포함 할 수 있는 새 클래스를 작성할 수 있다. (서로 다른 두 기능을 수행하는 클래스는 분리시키라는 의미인 듯.)

인라인 클래스 : 한 클래스가 다른 클래스에 흡수 될 수 있도록 거의 또는 전혀 기능을 수행하지 않는 경우

대리자 숨기기 : 클라이언트 클래스가 클래스 A의 메소드를 호출 한 다음 리턴 된 값을 사용하여 클래스 B의 메소드를 호출 할 때 클래스 A를 클래스 B로 호출하여 이를 단순화하고 클라이언트 클래스에 최종 값을 리턴 할 수 있다.

중개자 제거 : 이것은 대리자를 숨기는 것과 거의 반대입니다. 다른 개체에 대리하는 메서드가 너무 많으면 이러한 메서드를 삭제하고 클라이언트가 끝점을 직접 호출하도록 할 수 있다.

외부 메소드 도입 : 사용중인 유틸리티 클래스에 필요한 메소드가 포함되어 있지 않은 경우 클래스에서 작업을 수행하고 유틸리티 클래스를 매개 변수로 사용하는 메소드를 도입 할 수 있다.
	```
	Before refactoring:
	object Report {
		//...
		
		def getReport() {
			//...
			var nextDay = Date(previousDay.getYear, previousDay.getMonth, previousDay.getDay + 1)
			// ...
		}
	}

	After refactoring:
	object Report {
		//...
		
		def getReport() {
			//...
			var startDate = nextDay(previousDay)
			// ...
		}

		def nextDay(date: Date) {
			Date(previousDay.getYear, previousDay.getMonth,	previousDay.getDay + 1)
		}
	}
	```

로컬 확장 도입 : 이 기술은 외부 메서드를 도입하여 해결 된 것과 유사한 문제를 해결한다. 유틸리티 클래스에 필요한 몇 가지 메소드가 없는 경우 원래 유틸리티 클래스를 확장하거나 랩퍼로 작동 할 수 있는 모든 메소드를 새 클래스에 추가 할 수 있다.

### Organizing data
자체 캡슐화 필드 : 클래스의 프라이빗 필드에 직접 액세스 해야 하는 경우 프라이빗 필드에 대한 게터 및 세터를 도입 할 수 있다.
	```
	Before refactoring:
	class Foo(private val _title: String) {
		def printTitle(b: Book) {
			println(b._title)
		}
	}

	After refactoring:
	class Book(private val _title: String) {
		def title = _title
		def printTitle(b: Book) {
			println(b.title)
		}
	}
	```

데이터 값을 객체로 교체 : 클래스에 자체 동작과 데이터가 있는 데이터 필드가 포함 된 경우 새 클래스를 만들고 필드와 해당 동작을 새 클래스로 이동할 수 있다.

참조 값 변경: 동일한 클래스의 동일한 인스턴스가 많을 때, 모두 단일 참조 개체로 대체할 수 있다.

값에 대한 참조 변경 : 참조 객체가 너무 보잘 것 없는 경우 자체의 수명주기를 가지면 값 객체로 바꾸는 것이 좋다.

배열을 객체로 대체 : 다양한 유형의 데이터가 있는 배열은 더 의미있는 객체로 대체할 수 있다.

GUI 표출 데이터 중복 : GUI에 사용되는 도메인 데이터의 경우 다른 클래스에 별도로 구성하는게 좋다.

매직 넘버를 심볼릭 상수로 전환
    ```
    Before refactoring:
	def area(radius:Double) = 2 * 3.14159 * radius

	After refactoring:
	val PI = 3.14159
	def area(radius:Double) = 2 * PI * radius
    ```

타입코드를 클래스로 추출


## Summary




