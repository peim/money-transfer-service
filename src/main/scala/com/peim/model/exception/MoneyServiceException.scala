package com.peim.model.exception

class MoneyServiceException(private val message: String = "",
                            private val cause: Throwable = None.orNull)
  extends Exception(message, cause)
