package com.packt

import org.specs2.mutable._

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

class ExampleAcceptanceSpec extends Specification { def is =
  "Our example specification" ^
    "and we should run t1 here" ! t1 ^
    "and we should run t2 here" ! t2
  def t1 = success
  def t2 = pending
}

class ExampleSequentialAcceptanceSpec extends Specification { def is =
  args(sequential = true) ^
    "This is an example specification" ^
    "and this should run t1"          ! t1 ^
    "and this example should run t2"  ! t2
  def t1 = success
  def t2 = pending
}


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

class EmployeeAcceptanceSpec extends Specification {
  def is =
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
