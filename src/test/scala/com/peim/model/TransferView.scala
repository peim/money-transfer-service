package com.peim.model

case class TransferView(id: Int, sourceAccountId: Int, destAccountId: Int, currencyId: Int,
                        sum: Double, status: TransferStatus)

object TransferView extends ((Int, Int, Int, Int, Double, TransferStatus) => TransferView) {

  def apply(t: Transfer): TransferView =
    new TransferView(t.id, t.sourceAccountId, t.destAccountId, t.currencyId, t.sum, t.status)
}
