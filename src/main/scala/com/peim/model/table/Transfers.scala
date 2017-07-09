package com.peim.model.table

import com.peim.model.Transfer
import slick.driver.H2Driver.api._
import slick.lifted.Tag

class Transfers(tag: Tag) extends Table[Transfer](tag, "transfers") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def sourceAccountId = column[Int]("source_account_id")
  def destAccountId = column[Int]("dest_account_id")
  def currencyId = column[Int]("currency_id")
  def sum = column[Double]("sum")
  def status = column[String]("status")

  def * = (id, sourceAccountId, destAccountId, currencyId, sum, status) <> (Transfer.tupled, Transfer.unapply)
}
