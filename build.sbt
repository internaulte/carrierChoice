ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "carrierChoice",
    libraryDependencies ++= Seq(
      "com.lihaoyi"       %% "cask"        % "0.9.1",
      "org.scalatest"     %% "scalatest"   % "3.2.16"   % Test,
      "org.scalatestplus" %% "mockito-4-6" % "3.2.15.0" % Test
    ),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-Xfatal-warnings"
    )
  )
