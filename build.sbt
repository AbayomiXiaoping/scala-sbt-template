import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._

enablePlugins(UniversalPlugin)

/*
* Credential and other Environment Configuration
 */

lazy val systemUser: String = sys.env("ARTIFACTORY_SYS_USER")
lazy val systemPassword: String = sys.env("ARTIFACTORY_SYS_PASSWORD")
lazy val overwriteArtifact: Boolean = sys.env.getOrElse("ARTIFACTORY_OVERWRITE", "false").toBoolean

/*
* Project Configuration
 */
organization := "com.github.suriyakrishna"
name := "scala-sbt-template"
version := "1.0.0"
scalaVersion := "2.11.12"
maintainer := "suriya.kishan@live.com"
packageSummary := "Scala SBT Template"
packageDescription :=
  """Scala SBT Template"""

/*
* Assembly Plugin Configuration
 */

assemblyJarName in assembly := s"${name.value}.jar"

/*
* Project dependency Configuration
 */

resolvers ++= Seq(
  DefaultMavenRepository
)

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

/* *
* Publish Configuration
 */

artifact in(Compile, assembly) := {
  val art = (artifact in(Compile, assembly)).value
  art.withClassifier(some("assembly")) // This can be customized
}

artifact in(Universal, packageBin in Universal) := {
  val art = (artifact in(Universal, packageBin in Universal)).value
  art.withClassifier(Some("zip"))
  art.withExtension("zip").withType("zip")
}

addArtifact(artifact in(Compile, assembly), assembly)
addArtifact(artifact in(Universal, packageBin in Universal), packageBin in Universal)

publishMavenStyle := true
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
publishTo := Some("Artifactory Realm" at "https://suriyakrishna.jfrog.io/artifactory/package-generic-local/")
credentials += Credentials("Artifactory Realm", "suriyakrishna.jfrog.io", systemUser, systemPassword)