package com.packt.mock

class MatchResultObserver(playerDatabase: PlayerDatabase, countryLeaderBoard: CountryLeaderboard) {
  def recordMatchResult(result: MatchResult): Unit = {
    val player = playerDatabase.getPlayerById(result.winner)
    countryLeaderBoard.addVictoryForCountry(player.country)
  }
}