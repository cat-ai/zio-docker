ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "3.1.2"

val zioVersion = "2.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-docker",
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"               % zioVersion  % Provided,
      "dev.zio"                %% "zio-streams"       % zioVersion  % Provided,
      "com.github.docker-java"                      %  "docker-java"       %  "3.0.14",
      "ch.qos.logback"                              %  "logback-classic"   % "1.2.10",
      "dev.zio"                %% "zio-test"          % zioVersion  % Test,
      "dev.zio"                %% "zio-test-sbt"      % zioVersion  % Test,
      "dev.zio"                %% "zio-test-junit"    % zioVersion  % Test,
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )