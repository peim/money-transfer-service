package com.peim.model

import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Transfer(id: Int, sourceAccountId: Int, destAccountId: Int,
                    currencyId: Int, sum: Double, status: String) {
  def approved(newId: Int): Transfer = {
    new Transfer(newId, sourceAccountId, destAccountId, currencyId, sum, "approved")
  }
  def canceled(newId: Int): Transfer = {
    new Transfer(newId, sourceAccountId, destAccountId, currencyId, sum, "canceled")
  }
}

object Transfer extends ((Int, Int, Int, Int, Double, String) => Transfer) {
  implicit val transferReads: Reads[Transfer] = (
    (__ \ "id").read[Int] and
      (__ \ "sourceAccountId").read[Int] and
      (__ \ "destAccountId").read[Int] and
      (__ \ "currencyId").read[Int] and
      (__ \ "sum").read[Double] and
      (__ \ "status").readNullable[String].map(_.getOrElse("processing"))
    )(Transfer.apply _)

  implicit val transferWrites: Writes[Transfer] = Writes[Transfer] {
    t => Json.obj(
      "id" -> t.id,
      "sourceAccountId" -> t.sourceAccountId,
      "destAccountId" -> t.destAccountId,
      "currencyId" -> t.currencyId,
      "sum" -> t.sum,
      "status" -> t.status
    )
  }
}
