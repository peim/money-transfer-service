package com.peim.repository

import com.peim.model.Account
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class AccountsRepository(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val db = inject[DatabaseService].db

  def findAll: Future[Seq[Account]] = db.run(accounts.result)

  def findById(id: Int): Future[Option[Account]] =
    db.run(accounts.filter(_.id === id).result.headOption)

  def create(account: Account): Future[Int] =
    db.run(accounts returning accounts.map(_.id) += account)

  def updateBalance(id: Int, newBalance: Double): Future[Int] =
    db.run(accounts.filter(_.id === id)
      .map(old => old.balance)
      .update(newBalance))

  def delete(id: Int): Future[Int] =
    db.run(accounts.filter(_.id === id).delete)
}
