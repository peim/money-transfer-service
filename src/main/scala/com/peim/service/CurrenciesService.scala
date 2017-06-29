package com.peim.service

import com.peim.model.Currency
import com.peim.model.table._
import com.peim.utils.{BootData, DatabaseService}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class CurrenciesService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) {

  import databaseService._

  def getCurrencies: Future[Seq[Currency]] = db.run(currencies.result)

  def createCurrency(currency: Currency): Future[Int] =
    db.run(currencies returning currencies.map(_.id) += currency)

  private val setup = DBIO.seq(
    currencies.schema.create,
    currencies ++= BootData.getCurrencies
  )

  db.run(setup)
}
