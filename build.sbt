ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "carrierChoice"
  )

libraryDependencies += "com.lihaoyi"   %% "cask"        % "0.9.1"
libraryDependencies += "dev.zio"       %% "zio-prelude" % "1.0.0-RC18"
libraryDependencies += "org.scalatest" %% "scalatest"   % "3.2.15" % Test
