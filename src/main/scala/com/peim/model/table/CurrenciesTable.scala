package com.peim.model.table

import com.peim.model.Currency
import com.peim.utils.AdvancedH2Driver.api._
import slick.lifted.Tag

trait CurrenciesTable {

  class Currencies(tag: Tag) extends Table[Currency](tag, "currencies") {
    def id = column[Int]("id")
    def code = column[String]("code")

    def * = (id, code) <> (Currency.tupled, Currency.unapply)

    def pkCurrencies = primaryKey("pk_currencies", id)
  }

  protected val currencies = TableQuery[Currencies]
}
