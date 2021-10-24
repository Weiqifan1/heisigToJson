name := "heisigToJson"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"

val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.13.0")

lazy val root = (project in file("."))
  .settings(
    name := "circe-crash-course",
    libraryDependencies ++= circe
  )

libraryDependencies += "com.lihaoyi" %% "upickle" % "1.3.8" // SBT