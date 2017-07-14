package com.peim.model.exception

class IllegalTransferStatusException(private val message: String = "",
                                     private val cause: Throwable = None.orNull)
  extends MoneyServiceException(message, cause)
