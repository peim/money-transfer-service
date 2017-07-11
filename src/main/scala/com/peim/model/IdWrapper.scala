package com.peim.model

import play.api.libs.json.{Format, Json}

case class IdWrapper(id: Int)

object IdWrapper extends (Int => IdWrapper) {
  implicit val currencyFormat: Format[IdWrapper] = Json.format[IdWrapper]
}
