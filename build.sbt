name := "Test2"

version := "0.1"

scalaVersion := "2.12.8"
scalacOptions := Seq(  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "3.2.1" % "test,it"
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.2.1" % "test,it"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"

javacOptions in Gatling := overrideDefaultJavaOptions("-Xms2048m", "-Xmx2048m")
