package com.peim.model

import java.sql.Timestamp
import java.time.{OffsetDateTime, ZoneOffset}

import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import slick.driver.H2Driver.api._

case class Transfer(id: Int, sourceAccountId: Int, destAccountId: Int, currencyId: Int,
                    sum: Double, created: OffsetDateTime, status: String) {
  def approved(newId: Int): Transfer = {
    new Transfer(newId, sourceAccountId, destAccountId, currencyId, sum, created, "approved")
  }
  def canceled(): Transfer = {
    new Transfer(id, sourceAccountId, destAccountId, currencyId, sum, created, "canceled")
  }
}

object Transfer extends ((Int, Int, Int, Int, Double, OffsetDateTime, String) => Transfer) {
  implicit val transferReads: Reads[Transfer] = (
    (__ \ "id").read[Int] and
      (__ \ "sourceAccountId").read[Int] and
      (__ \ "destAccountId").read[Int] and
      (__ \ "currencyId").read[Int] and
      (__ \ "sum").read[Double] and
      (__ \ "created").read[OffsetDateTime] and
      (__ \ "status").readNullable[String].map(_.getOrElse("processing"))
    )(Transfer.apply _)

  implicit val transferWrites: Writes[Transfer] = Writes[Transfer] {
    t => Json.obj(
      "id" -> t.id,
      "sourceAccountId" -> t.sourceAccountId,
      "destAccountId" -> t.destAccountId,
      "currencyId" -> t.currencyId,
      "sum" -> t.sum,
      "created" -> t.created,
      "status" -> t.status
    )
  }

  implicit val offsetDateTimeTypeMapper = MappedColumnType.base[OffsetDateTime, Timestamp](
    l => Timestamp.from(l.toInstant),
    t => OffsetDateTime.ofInstant(t.toInstant, ZoneOffset.UTC)
  )
}
