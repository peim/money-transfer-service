package com.peim.model

import play.api.libs.json._
import slick.driver.H2Driver.api._

import scala.util.Try

sealed trait TransferStatus
case object Approved extends TransferStatus
case object Processing extends TransferStatus
case object Canceled extends TransferStatus

object TransferStatus {

  implicit val transferStatusReads: Reads[TransferStatus]  = Reads[TransferStatus] {
    case JsString(str) => Try(TransferStatus.fromString(str)).map(JsSuccess(_))
      .getOrElse(JsError("Unrecognized transfer status"))
    case _ => JsError("Transfer status should be a string")
  }

  implicit val transferStatusWrites: Writes[TransferStatus] =
    Writes[TransferStatus](v => JsString(TransferStatus.toString(v)))

  implicit val transferStatusTypeMapper = MappedColumnType.base[TransferStatus, String](
    l => TransferStatus.toString(l),
    t => TransferStatus.fromString(t))

  def fromString(str: String): TransferStatus = str match {
    case "approved" => Approved
    case "processing" => Processing
    case "canceled" => Canceled
    case _ => throw new RuntimeException(s"Unrecognized transfer status: $str")
  }

  def toString(value: TransferStatus): String = value match {
    case Approved => "approved"
    case Processing => "processing"
    case Canceled => "canceled"
  }
}
