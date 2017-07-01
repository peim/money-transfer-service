package com.peim.service

import com.peim.model.Transfer
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class TransfersService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val db = inject[DatabaseService].db

  def findAll: Future[Seq[Transfer]] = db.run(transfers.result)

  def findById(id: Int): Future[Option[Transfer]] =
    db.run(transfers.filter(_.id === id).result.headOption)

  def create(transfer: Transfer): Future[Int] = {
    db.run(transfers returning transfers.map(_.id) += transfer)
  }
}
