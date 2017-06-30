package com.peim.service

import com.peim.model.User
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val databaseService = inject[DatabaseService]

  import databaseService._

  def getUsers: Future[Seq[User]] = db.run(users.result)

  def createUser(user: User): Future[Int] =
    db.run(users returning users.map(_.id) += user)
}
