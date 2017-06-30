package com.peim.service

import com.peim.model.User
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val db = inject[DatabaseService].db

  def findAll: Future[Seq[User]] = db.run(users.result)

  def findById(id: Int): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def create(user: User): Future[Int] = {
    db.run(users returning users.map(_.id) += user)
  }

  def update(id: Int, user: User): Future[Int] =
    db.run(users.filter(_.id === id)
      .map(old => old.name)
      .update(user.name))

  def delete(id: Int): Future[Int] =
    db.run(users.filter(_.id === id).delete)
}
