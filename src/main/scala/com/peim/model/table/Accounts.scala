package com.peim.model.table

import com.peim.model.Account
import slick.driver.H2Driver.api._
import slick.lifted.Tag

class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
  def id = column[Int]("id", O.PrimaryKey)
  def userId = column[Int]("user_id")
  def currencyId = column[Int]("currency_id")
  def balance = column[Double]("balance")

  def * = (id, userId, currencyId, balance) <> (Account.tupled, Account.unapply)

  def fkAccountsUsers = foreignKey("fk_accounts_users", userId, users)(_.id)
  def fkAccountsCurrencies = foreignKey("fk_accounts_currencies", currencyId, currencies)(_.id)
}
