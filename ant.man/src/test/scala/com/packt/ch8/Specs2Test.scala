package com.packt.ch8

import org.specs2.mutable._

class Specs2Test extends Specification {
  /*
  class FootballTeam private (val teamName, val players:Option[List[Player]]) {
    def getGoalkeeper:Player {
      players.getOrElse.filter(_.position="Goal Keeper")
    }
  }

  "A Football Team" should {
    """have a getGoalKeeper function which
selects the plater who has been earmarked to be the goalkeeper""" in {
      val firstTeam = new FootballTeam("Gaurav's Team", 2016,
        Some(List(new Player("Tom", "Midfielder"),
          new Player("Freddy", "Midfielder"),
          new Player("Steve", "Center Forward"),
          new Player("Dale", "Goal Keeper"),))
      )
      firstTeam.getGoalkeeper.get.name must be_==("Dale")
    }
  }*/

  "this is another specification" >> {
    "where first example must be true" >> {
      "Hello" must_== "Hello"
    }
    "where second specification must be false" >> {
      "World" must_!= "Earth"
    }
  }

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}
