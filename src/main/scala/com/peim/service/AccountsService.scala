package com.peim.service

import com.peim.model.Account
import com.peim.model.table.AccountsTable
import com.peim.utils.DatabaseService
import com.peim.utils.AdvancedH2Driver.api._
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class AccountsService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends AccountsTable {

  import databaseService._

  def getAccounts: Future[Seq[Account]] = {
    db.run(sql"SELECT * FROM INFORMATION_SCHEMA.TABLES".as[JsValue]).onComplete{
      case Success(d) => println("!!! " + d)
      case Failure(e) => println(e)
    }

    db.run(accounts.result)
  }

  def createAccount(account: Account): Future[Account] =
    db.run(accounts returning accounts += account)

}
