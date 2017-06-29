package com.peim.model.table

import com.peim.model.Currency
import slick.driver.H2Driver.api._
import slick.lifted.Tag

trait CurrenciesTable {

  class Currencies(tag: Tag) extends Table[Currency](tag, "currencies") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def code = column[String]("code")

    def * = (id, code) <> (Currency.tupled, Currency.unapply)
  }

  protected val currencies = TableQuery[Currencies]
}
