package com.peim.utils

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, MethodRejection, RejectionHandler, ValidationRejection}
import com.peim.model.exception.MoneyServiceException
import com.peim.model.response.ErrorResponse
import play.api.libs.json.Json

trait ExceptionHandlers {

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: MoneyServiceException =>
        extractUri { uri =>
          val errorResponse = ErrorResponse(Conflict.intValue,
            "Conflict", e.getMessage)
          val entity = HttpEntity(ContentTypes.`application/json`, Json.toJson(errorResponse).toString)
          complete(HttpResponse(Conflict, entity = entity))
        }
      case e: Exception =>
        extractUri { uri =>
          val errorResponse = ErrorResponse(InternalServerError.intValue,
            "Internal Server Error", e.getMessage)
          val entity = HttpEntity(ContentTypes.`application/json`, Json.toJson(errorResponse).toString)
          complete(HttpResponse(InternalServerError, entity = entity))
        }
    }

  implicit def rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleNotFound {
        val errorResponse = ErrorResponse(NotFound.intValue,
          "NotFound", "The requested resource could not be found.")
        val entity = HttpEntity(ContentTypes.`application/json`, Json.toJson(errorResponse).toString)
        complete(HttpResponse(NotFound, entity = entity))
      }
      .handle { case ValidationRejection(message, _) =>
        val errorResponse = ErrorResponse(BadRequest.intValue, "Bad Request", message)
        val entity = HttpEntity(ContentTypes.`application/json`, Json.toJson(errorResponse).toString)
        complete(HttpResponse(BadRequest, entity = entity)) }
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        val errorResponse = ErrorResponse(MethodNotAllowed.intValue,
          "Not Allowed", s"Access to $names is not allowed.")
        val entity = HttpEntity(ContentTypes.`application/json`, Json.toJson(errorResponse).toString)
        complete(HttpResponse(MethodNotAllowed, entity = entity))
      }
      .result()
}
