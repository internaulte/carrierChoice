ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "carrierChoice"
  )

libraryDependencies += "com.lihaoyi"   %% "cask"        % "0.8.3"
libraryDependencies += "dev.zio"       %% "zio-prelude" % "1.0.0-RC16"
libraryDependencies += "org.scalatest" %% "scalatest"   % "3.2.14" % Test
