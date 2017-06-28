package com.peim

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.utils.{Config, DatabaseMigration}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

object Main extends App with Config with LazyLogging{

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Try(DatabaseMigration.migrate()) match {
    case Success(_) =>
      val routes = new HttpService().routes
      val loggedRoutes = DebuggingDirectives.logRequestResult("Request:: ", Logging.InfoLevel)(routes)

      Http().bindAndHandle(loggedRoutes, httpHost, httpPort).map {
        binding => log.info("REST interface bound to {}", binding.localAddress)
      }.recover {
        case error: Exception =>
          log.error(error, s"REST interface could not bind to $httpHost:$httpPort")
          actorSystem.terminate()
      }
    case Failure(e) =>
      logger.error("Database migration failed", e)
  }
}
