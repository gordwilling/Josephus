name := "Josephus"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test"
libraryDependencies += "org.scalacheck" % "scalacheck_2.11" % "1.13.4"

mainClass in Compile := Some("com.highbar.josephus.Main")
