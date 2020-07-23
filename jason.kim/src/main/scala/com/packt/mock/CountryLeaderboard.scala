package com.packt.mock

import com.packt.mock.Player.Country

case class CountryLeaderboardEntry(country: Country, points: Int)

trait CountryLeaderboard {
  def addVictoryForCountry(country: Country): Unit
  def getTopCountries(): List[CountryLeaderboardEntry]
}

