enablePlugins(ScalaJSPlugin)
scalaJSUseRhino in Global := false

name := "typist"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.0",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
  "com.lihaoyi" %%% "scalatags" % "0.6.0",
  "io.monix" %%% "monix" % "2.0-RC10"
)

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"
)
