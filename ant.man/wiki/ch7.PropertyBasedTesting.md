# 7장 property 기반 테스트
가능한 많은 테스트를 해야함 -> 입력을 생산해서 테스트

- Property-based testing
- Table-driven property checks
- Generator-driven property checks
- ScalaCheck

## Introduction to property-based testing
- 테스트는 sw 품질의 척도임. 근데 테스트 커버리지가 좋은게 충분한 테스트를 뜻하진 않음
  - 소프트웨어의 품질은 테스트 품질에 따라 다름 -> 테스트가 좋을수록 소프트웨어 품질이 높아짐 -> 좋은 테스트를 작성하는 것이 매우 어렵다는 것을 의미
- 그 대안으로 속성 기반 테스트를 사용하여 광범위한 입력 범위에서 동작을 테스트 할 수 있습니다. ->  다양한 입력에 적합하도록 테스트됨
  -  테스트가 몇 개 (3 개 또는 4 개)만으로 동작을 테스트하는 전통적인 테스트 방법과 대조

## Table-driven properties
- 테스트 데이터가 유한 범위에 있으면 입력 테이블에 넣을 수 있음
  - 테이블 중심 테스트는 테스트를 실행하려는 명확한 값 범위가있을 때 유용

```scala
class BinaryToDecimalSpec extends UnitSpec with TableDrivenPropertyChecks {
  it should "convert binary to decimal" in {
    val validCombos =
      Table(
        ("100100111101",  "2365"),
        ("11110001111110111",  "123895"),
        ("100000000000001110000001",  "8389505"),
        ("1011110101011101001101",  "3102541")
      )
    forAll(validCombos) { (binString:String, decString:String) =>
      var decimal = BaseConversion.binaryToDecimal(Binary(binString))
      decimal.number shouldBe decString
    }
  }
}
```

## Generator-driven properties
- 때로는 무작위로 생성된 입력 집합에 대해 속성을 확인하기를 원함
  - 테스트가 필요한 압축 알고리즘이있는 경우를 예
  - 사전 정의 된 문자열 목록을 사용하는 대신 생성 된 문자열 세트를 사용하여 함수에 대해 테스트

## ScalaCheck
- ScalaCheck 라이브러리는 생성기 기반의 속성 기반 테스트에 사용
  - https://www.scalacheck.org/

- quick start
```scala
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object StringSpecification extends Properties("String") {

  property("startsWith") = forAll { (a: String, b: String) =>
    (a+b).startsWith(a)
  }

  property("concatenate") = forAll { (a: String, b: String) =>
    (a+b).length > a.length && (a+b).length > b.length
  }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a+b+c).substring(a.length, a.length+b.length) == b
  }

}
```

### Generators
- ScalaCheck은 테스트 데이터 생성을 위해 생성기를 사용
  -  생성 된 값은 생성 매개 변수와 직접적인 상관 관계

  - 아래 예제에서 보면 s2는 s1의 무조건 두배
```scala
val strings = (for {
s1 <- Gen.alphaStr
s2 <- Gen.alphaStr
} yield (s1, s2)).suchThat( m => m._2.length > m._1.length * 2 )
```

- 아래 중 하나
```scala
val vowel = Gen.oneOf('A', 'E', 'I', 'O', 'U', 'Y')
```
- 아래 중 하나인데, 빈도를 달리해서
```scala
val vowel = Gen.frequency(
  (3, 'A'),
  (4, 'E'),
  (2, 'I'),
  (3, 'O'),
  (1, 'U'),
  (1, 'Y')
)
```
- Generating case class
```
val epayeOrders: Arbitrary[EPayeOrder] = Arbitrary((for {
  districtNumber <- Gen.containerOfN(3, Gen.numChar)
  checkCharater <- Gen.alphaChar
  registerNumber <- Gen.containerOfN(6, Gen.numChar)
  registerAlphaNum <- Gen.containerOfN(2, Gen.alphaNumChar)
  month <- Gen.chooseNum(1, 12)
  year <- Gen.chooseNum(10, 99)
    } yield EPayeOrder (
      Seq.empty[Char] ++
        districtNumber ++
        Seq('P', checkCharater) ++
        registerNumber ++
        registerAlphaNum ++ year.toString.toCharArray ++
          StringUtils.leftPad(month.toString, 2, '0').toCharArray)
        .mkString.toUpperCase)
        .suchThat(_.length == 17)
    )
```
- Conditional Generators (we generate prime numbers between given range.)
```scala
val primes = for {
  s1 <- Gen.chooseNum(10, 100).suchThat(n => !((2 until n/2)
  exists(n%_==0)))
} yield s1
```
- Generating containers (containerOf, nonEmptyContainerOf, and containerOfN)
```
val genIntList = Gen.containerOf[List,Int](Gen.oneOf(4, 6, 7))
```
- Arbitrary Generator
`val string = Arbitrary.arbitrary[String] suchThat (_.length > 10)`
- Generation statistics
```
import org.scalacheck.Prop._
val prop1 = forAll { l: Seq[String] =>
  classify(ordered(l), "sorted") {
    classify(l.length > 5, "long", "tiny") {
      l.reverse.reverse == l
    }
  }
}
```

### Executing property checks
- 가장 쉬운방법으로 속성에서 `check` 를 호출

- https://github.com/typelevel/scalacheck/blob/master/doc/UserGuide.md
- https://www.scalacheck.org/files/scaladays2014/index.html#1

### Our own Generator-driven property checks

```scala
import com.packt.packt.Decimal
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen

class DecimalToBinaryGenSpec extends UnitSpec with
  GeneratorDrivenPropertyChecks {
  it should "convert decimal to binary and back to decimal" in {
    val decimals = (for {
      c1 <- Gen.chooseNum(2, 100000)
    } yield c1.toString).suchThat(_ != "")
    forAll(decimals) { (decimalStr: String) =>
      var binary = BaseConversion.decimalToBinary(Decimal(decimalStr))
      var decimal = BaseConversion.binaryToDecimal(binary)
      decimal.number shouldBe decimalStr
    }
  }

  it should "convert decimal to hexadecimal and back to decimal" in {
    val decimals = (for {
      c1 <- Gen.chooseNum(2,100000)
    } yield c1.toString).suchThat(_ != "")
    forAll(decimals){ (decimalStr:String) =>
      var hex = BaseConversion.decimalToHexadecimal(Decimal(decimalStr))
      var decimal = BaseConversion.hexadecimalToDecimal(hex)
      decimal.number shouldBe decimalStr
    }
  }
}
```

## Summary
- 이 장에서 속성 기반 테스트가 중복 테스트를 제거하고 최대 적용 범위를 갖는 것이 얼마나 유용한 지 봤음
- table-driven and Generator-driven property checks 둘다 적재적소에 써야함
  - Generator-driven checks 는 문서화 하기 힘듦

## 추가 참조하기 좋은 자료

- https://github.com/typelevel/scalacheck/blob/master/doc/UserGuide.md
- http://charleso.github.io/property-testing-preso/yowlj2015.html
- https://tyrcho.github.io/pbt-kata/#1
- https://www.scalacheck.org/files/scaladays2014/index.html#1
