package com.packt.mock

import com.packt.mock.Player.{Country, PlayerId}

object Player {
  type PlayerId = Int
  type Country = String
}

case class Player(id: PlayerId, nickname: String, country: Country)
case class MatchResult(winner: PlayerId, loser: PlayerId)

