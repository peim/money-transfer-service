organization  := "com.peim"

name := "money-transfer-service"

version := "1.0"

scalaVersion := "2.12.1"

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
  val akkaHttpVersion = "10.0.8"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

    "de.heikoseeberger" %% "akka-http-play-json" % "1.12.0",

    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
}
