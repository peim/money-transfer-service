package com.peim.repository

import com.peim.model.Currency
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class CurrenciesRepository(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val db = inject[DatabaseService].db

  def findAll: Future[Seq[Currency]] = db.run(currencies.result)

  def findById(id: Int): Future[Option[Currency]] =
    db.run(currencies.filter(_.id === id).result.headOption)

  def create(currency: Currency): Future[Int] =
    db.run(currencies returning currencies.map(_.id) += currency)

  def delete(id: Int): Future[Int] =
    db.run(currencies.filter(_.id === id).delete)
}
