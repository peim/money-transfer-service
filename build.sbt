organization  := "com.peim"

name := "money-transfer-service"

version := "1.0"

scalaVersion := "2.12.1"

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
  val akkaVersion = "2.5.3"
  val akkaHttpVersion = "10.0.9"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

    "de.heikoseeberger" %% "akka-http-play-json" % "1.12.0",
    "com.typesafe.slick" %% "slick" % "3.2.0",

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
