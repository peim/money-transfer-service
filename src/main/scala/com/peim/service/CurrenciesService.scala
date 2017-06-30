package com.peim.service

import com.peim.model.Currency
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class CurrenciesService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val databaseService = inject[DatabaseService]

  import databaseService._

  def getCurrencies: Future[Seq[Currency]] = db.run(currencies.result)

  def createCurrency(currency: Currency): Future[Int] =
    db.run(currencies returning currencies.map(_.id) += currency)
}
