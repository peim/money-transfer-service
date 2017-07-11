package com.peim.model

import play.api.libs.json.{Format, Json}

case class Currency(id: Int, code: String)

object Currency extends ((Int, String) => Currency) {
  implicit val currencyFormat: Format[Currency] = Json.format[Currency]
}
