package com.peim

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.service.AccountsService
import com.peim.utils.{Config, DatabaseMigration, DatabaseService}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

object Main extends App with Config with LazyLogging{

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

//  Try(DatabaseMigration.migrate(driver, url, user, password)) match {
//    case Success(_) =>
      val databaseService = new DatabaseService
      val accountsService = new AccountsService(databaseService)

      val routes = new HttpService(accountsService).routes
      val loggedRoutes = DebuggingDirectives.logRequestResult("Request:: ", Logging.InfoLevel)(routes)

      Http().bindAndHandle(loggedRoutes, httpHost, httpPort).map {
        binding => log.info("REST interface bound to {}", binding.localAddress)
      }.recover {
        case e: Exception =>
          log.error(e, s"REST interface could not bind to $httpHost:$httpPort")
          actorSystem.terminate()
      }
//    case Failure(e) =>
//      logger.error("Database migration failed", e)
//  }
}
