import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._

enablePlugins(UniversalPlugin)
enablePlugins(UniversalDeployPlugin)

name := "scala-sbt-template"

version := "2.0.0"

scalaVersion := "2.11.12"

maintainer := "suriya.kishan@live.com"

packageSummary := "Scala SBT Template"

packageDescription :=
  """Scala SBT Template"""

/*
* Assembly Plugin Configuration
 */

assemblyJarName in assembly := s"${name.value}.jar"

artifact in(Compile, assembly) := {
  val art = (artifact in(Compile, assembly)).value
  art.withClassifier(some("assembly")) // This can be customized
}

addArtifact(artifact in(Compile, assembly), assembly)

// Project Library Dependencies configuration
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.7.4"
)

/* *
* Universal Plugin Configuration
 */

// Configure Universal Name
name in Universal := name.value
name in UniversalDocs := (name in Universal).value
name in UniversalSrc := (name in Universal).value
packageName in Universal := packageName.value

// Add files to be in final Universal Package
mappings in Universal ++= directory("bin")
mappings in Universal ++= directory("conf")
//mappings in Universal += (assembly in Compile).value -> s"lib/${name.value}.jar"