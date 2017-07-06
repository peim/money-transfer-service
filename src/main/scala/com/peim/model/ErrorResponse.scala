package com.peim.model

import play.api.libs.json.{Format, Json}

case class ErrorResponse(code: Int, `type`: String, message: String)

object ErrorResponse {
  implicit val errorResponseFormat: Format[ErrorResponse] = Json.format[ErrorResponse]
}
