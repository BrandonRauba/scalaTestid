ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

libraryDependencies += "io.github.hughsimpson" %% "scalameter" % "0.22.1"

lazy val root = (project in file("."))
  .settings(
    name := "Scala benchmarks"
  )
