package com.peim.utils

import com.peim.model.{Account, Currency, User}

object BootData {

  def getAccounts: Seq[Account] = Seq(
    Account(1, 1, 3, 500),
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
