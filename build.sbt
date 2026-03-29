ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.8.2"

lazy val root = (project in file("."))
  .settings(
    name := "carrierChoice",
    libraryDependencies ++= Seq(
      "com.lihaoyi"       %% "cask"        % "0.11.3",
      "org.scalatest"     %% "scalatest"   % "3.2.20"   % Test,
      "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0" % Test
    ),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-Xfatal-warnings"
    )
  )
