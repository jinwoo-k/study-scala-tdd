package com.packt.mock

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class CountyLeaderboardSpec extends AnyFlatSpec with MockFactory {
  "PlayerDatabase" should "work as expected." in {
    // create fakeDb stub that implements PlayerDatabase trait
    val fakeDb = stub[PlayerDatabase]

    // configure fakeDb behavior
    (fakeDb.getPlayerById _) when(222) returns(Player(222, "boris", Countries.Russia))
    (fakeDb.getPlayerById _) when(333) returns(Player(333, "hans", Countries.Germany))

    // use fakeDb
    assert(fakeDb.getPlayerById(222).nickname == "boris")
  }

  "CountryLoaderboard" should "work as expected." in {
    // create CountryLeaderboard mock
    val countryLeaderBoardMock = mock[CountryLeaderboard]

    // set expectations
    (countryLeaderBoardMock.addVictoryForCountry _).expects(Countries.Germany)

    // use countryLeaderBoardMock
    countryLeaderBoardMock.addVictoryForCountry(Countries.Germany) // OK
    countryLeaderBoardMock.addVictoryForCountry(Countries.Russia)  // throws TestFailedException
  }



  "MatchResultObserver" should "update CountryLeaderBoard after finished match" in {
    val countryLeaderBoardMock = mock[CountryLeaderboard]
    val userDetailsServiceStub = stub[PlayerDatabase]

    val winner = Player(id = 222, nickname = "boris", country = Countries.Russia)
    val loser = Player(id = 333, nickname = "hans", country = Countries.Germany)

    // set expectations
    (countryLeaderBoardMock.addVictoryForCountry _).expects(Countries.Russia)

    // configure stubs
    (userDetailsServiceStub.getPlayerById _).when(loser.id).returns(loser)
    (userDetailsServiceStub.getPlayerById _).when(winner.id).returns(winner)

    // run system under test
    val matchResultObserver = new MatchResultObserver(userDetailsServiceStub, countryLeaderBoardMock)
    matchResultObserver.recordMatchResult(MatchResult(winner = winner.id, loser = loser.id))
  }



}
