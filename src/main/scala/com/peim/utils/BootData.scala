package com.peim.utils

import com.peim.model.table.{accounts, currencies, users}
import com.peim.model.{Account, Currency, User}
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

class BootData(implicit inj: Injector) extends Injectable {

  import BootData._

  def load(): Unit = {
    val databaseService = inject[DatabaseService]
    databaseService.db.run(setup)
  }

  private val setup = DBIO.seq(
    (accounts.schema ++ users.schema ++ currencies.schema).create,
    currencies ++= getCurrencies,
    users ++= getUsers,
    accounts ++= getAccounts
  )
}

object BootData {

  def getAccounts: Seq[Account] = Seq(
    Account(1, 1, 2, 500),
    Account(2, 1, 1, 200),
    Account(3, 2, 2, 1000)
  )

  def getUsers: Seq[User] = Seq(
    User(1, "Alex"),
    User(2, "Max"),
    User(3, "Fred")
  )

  def getCurrencies: Seq[Currency] = Seq(
    Currency(1, "USD"),
    Currency(2, "EUR"),
    Currency(3, "GBR")
  )
}
