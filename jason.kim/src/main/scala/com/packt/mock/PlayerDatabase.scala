package com.packt.mock

import com.packt.mock.Player.PlayerId

trait PlayerDatabase {
  def getPlayerById(playerId: PlayerId): Player
}


//class RealPlayerDatabase(
//                          dbConnectionPool: DbConnectionPool,
//                          databaseConfig: DatabaseConfig,
//                          securityManager: SecurityManager,
//                          transactionManager: TransactionManager) extends PlayerDatabase {
//
//  override def getPlayerById(playerId: PlayerId) = ???
//}
