ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "carrierChoice"
  )

libraryDependencies += "com.lihaoyi"   %% "cask"        % "0.8.0"
libraryDependencies += "dev.zio"       %% "zio-prelude" % "1.0.0-RC13"
libraryDependencies += "org.scalatest" %% "scalatest"   % "3.2.11" % Test
