package com.peim.service

import com.peim.model.Currency
import com.peim.model.table.CurrenciesTable
import slick.driver.H2Driver.api._
import com.peim.utils.DatabaseService

import scala.concurrent.{ExecutionContext, Future}

class CurrenciesService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends CurrenciesTable {

  import databaseService._

  def getAccounts: Future[Seq[Currency]] = db.run(currencies.result)

  def createAccount(currency: Currency): Future[Currency] =
    db.run(currencies returning currencies += currency)

}
