package com.peim.utils

import com.peim.model.table._
import com.peim.model._
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

class BootData(implicit inj: Injector) extends Injectable {

  import BootData._

  def load(): Unit = {
    val databaseService = inject[DatabaseService]
    databaseService.db.run(setup)
  }

  private val setup = DBIO.seq(
    (accounts.schema ++ users.schema ++ currencies.schema ++ transfers.schema).create,
    currencies ++= getCurrencies,
    users ++= getUsers,
    accounts ++= getAccounts,
    transfers ++= getTransfers
  )
}

object BootData {

  def getCurrencies: Seq[Currency] = Seq(
    Currency(1, "USD"),
    Currency(2, "EUR"),
    Currency(3, "GBR")
  )

  def getUsers: Seq[User] = Seq(
    User(1, "Alex"),
    User(2, "Max"),
    User(3, "Fred")
  )

  def getAccounts: Seq[Account] = Seq(
    Account(1, 1, 2, 500),
    Account(2, 1, 1, 200),
    Account(3, 3, 1, 1000),
    Account(4, 3, 2, 800),
    Account(5, 2, 3, 400)
  )

  def getTransfers: Seq[Transfer] = Seq(
    Transfer(1, 1, 2, 200),
    Transfer(2, 3, 1, 300),
    Transfer(3, 2, 3, 100)
  )
}
