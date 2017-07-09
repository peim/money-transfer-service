package com.peim.model.table

import java.time.OffsetDateTime

import com.peim.model.{Transfer, TransferStatus}
import slick.driver.H2Driver.api._
import slick.lifted.Tag

class Transfers(tag: Tag) extends Table[Transfer](tag, "transfers") {

  import Transfer._

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def sourceAccountId = column[Int]("source_account_id")
  def destAccountId = column[Int]("dest_account_id")
  def currencyId = column[Int]("currency_id")
  def sum = column[Double]("sum")
  def created = column[OffsetDateTime]("created")
  def status = column[TransferStatus]("status")

  def * = (id, sourceAccountId, destAccountId, currencyId, sum, created, status) <> (Transfer.tupled, Transfer.unapply)
}
