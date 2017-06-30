organization  := "com.peim"

name := "money-transfer-service"

version := "1.0"

scalaVersion := "2.11.8"

parallelExecution in Test := false

scalacOptions := Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-Xfuture",
  "-encoding", "utf8"
)

libraryDependencies ++= {
  val akkaVersion = "2.4.17"
  val akkaHttpVersion = "10.0.3"
  val slickVersion = "3.1.1"
  val slickPgVersion = "0.14.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

    "de.heikoseeberger" %% "akka-http-play-json" % "1.12.0",
    "com.typesafe.slick" %% "slick" % slickVersion,

    "org.scaldi" %% "scaldi" % "0.5.8",
    "org.scaldi" %% "scaldi-akka" % "0.5.8",

    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

    "ch.qos.logback" % "logback-classic" % "1.2.1",
    "com.h2database" % "h2" % "1.4.196",

    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
}
