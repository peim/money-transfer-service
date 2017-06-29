package com.peim.service

import com.peim.model.Currency
import com.peim.model.table.CurrenciesTable
import com.peim.utils.DatabaseService
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class CurrenciesService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends CurrenciesTable {

  import databaseService._

  def getCurrencies: Future[Seq[Currency]] = db.run(currencies.result)

  def createCurrency(currency: Currency): Future[Int] =
    db.run(currencies returning currencies.map(_.id) += currency)

  private val setup = DBIO.seq(
    currencies.schema.create,

    currencies += Currency(1, "USD"),
    currencies += Currency(2, "EUR"),
    currencies += Currency(3, "GBR")
  )

  db.run(setup)
}
