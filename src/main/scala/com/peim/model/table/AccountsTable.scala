package com.peim.model.table

import java.util.UUID

import com.peim.model.Account
import slick.driver.H2Driver.api._
import slick.lifted.Tag

trait AccountsTable extends UsersTable with CurrenciesTable {

  class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
    def id = column[UUID]("id")
    def userId = column[UUID]("user_id")
    def currencyId = column[Int]("currency_id")
    def balance = column[Double]("balance")

    def * = (id, userId, currencyId, balance) <> (Account.tupled, Account.unapply)

    def pkAccounts = primaryKey("pk_accounts", id)
    def fkAccountsUsers = foreignKey("fk_accounts_users", userId, TableQuery[Users])(_.id)
    def fkAccountsCurrencies = foreignKey("fk_accounts_currencies", currencyId, TableQuery[Currencies])(_.id)
  }

  protected val accounts = TableQuery[Accounts]
}
