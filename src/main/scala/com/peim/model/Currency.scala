package com.peim.model

import play.api.libs.json.{Format, Json}

class Currency(id: Int, code: String)

object Currency {
  implicit val currencyFormat: Format[Currency] = Json.format[Currency]
}