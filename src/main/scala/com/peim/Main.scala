package com.peim

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.utils.Config

import scala.concurrent.ExecutionContext

object Main extends App with Config {

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val httpService = new HttpService()

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}
