# private 메소드 테스팅

## 권장

1. public 메소드로 간접테스트
2. 임의로 scope 를 protected 혹은 public 으로 넓히는 것은 바람직하지 않음
3. private 메소드가 하는일이 크다는 것은 별도의 클래스로 분리하거나, 하위 클래스에서 상속을 이용해 대체할 수 있을 수 있음.

## 굳이 하겠다면

[https://blog.benelog.net/2685835.html](https://blog.benelog.net/2685835.html)

[https://www.scalatest.org/user_guide/using_PrivateMethodTester](https://www.scalatest.org/user_guide/using_PrivateMethodTester)
