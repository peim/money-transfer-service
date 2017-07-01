package com.peim.model

import play.api.libs.json.{Format, Json}

case class Transfer(id: Int, sourceAccountId: Int, destAccountId: Int, sum: Double)

object Transfer extends ((Int, Int, Int, Double) => Transfer) {
  implicit val transferFormat: Format[Transfer] = Json.format[Transfer]
}
