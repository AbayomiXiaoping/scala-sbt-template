import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._

import java.io.FileWriter
import scala.sys.process._

enablePlugins(UniversalPlugin)

/*
 * Credential and other Environment Configuration
 */

lazy val systemUser: String = sys.env("ARTIFACTORY_SYS_USER")
lazy val systemPassword: String = sys.env("ARTIFACTORY_SYS_PASSWORD")
lazy val overwriteArtifact: Boolean =
  sys.env.getOrElse("ARTIFACTORY_OVERWRITE", "false").toBoolean

/*
 * Project Configuration
 */
organization := "com.github.suriyakrishna"
name := "scala-sbt-template"
version := "1.1.0"
scalaVersion := "2.11.12"
maintainer := "suriya.kishan@live.com"
packageSummary := "Scala SBT Template"
packageDescription :=
  """This repository provides files, directories, and build configuration for a Scala SBT project that can be completely 
    |inferred to create a new Scala SBT project with minimal changes.""".stripMargin

homepage := Some(url("https://github.com/suriyakrishna/scala-sbt-template"))
licenses := Seq(
  "GNU-gpl-3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html")
)

/*
 * Assembly Plugin Configuration
 */

assemblyJarName in assembly := s"${name.value}.jar"

/*
 * Project dependency Configuration
 */

resolvers ++= Seq(DefaultMavenRepository)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.mockito" %% "mockito-scala-scalatest" % "1.16.37" % Test
)

/* *
 * Jacoco Plugin Threshold Configuration
 */

jacocoReportSettings := JacocoReportSettings()
  .withThresholds(
    JacocoThresholds(
      instruction = 80,
      method = 100,
      branch = 100,
      complexity = 100,
      line = 90,
      clazz = 100
    )
  )
  .withTitle(s"${name.value} Jacoco Test Report")

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
mappings in Universal ++= directory(jacocoReportDirectory.value).map(a =>
  (a._1, s"jacoco/${a._2}")
)
//mappings in Universal += (assembly in Compile).value -> s"lib/${name.value}.jar"

/* *
 * Publish Configuration
 */

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(some("assembly")) // This can be customized
}

artifact in (Universal, packageBin in Universal) := {
  val art = (artifact in (Universal, packageBin in Universal)).value
  art.withExtension("zip").withType("zip")
}

artifact in writeGitMetadata := {
  val art = (artifact in writeGitMetadata).value
  art
    .withClassifier(Some("build-metadata"))
    .withExtension("log")
    .withType("audit")
}

addArtifact(artifact in (Compile, assembly), assembly)
addArtifact(
  artifact in (Universal, packageBin in Universal),
  packageBin in Universal
)
addArtifact(artifact in writeGitMetadata, writeGitMetadata)

publishMavenStyle := true
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
publishTo := Some(
  "Artifactory Realm" at "https://suriyakrishna.jfrog.io/artifactory/package-generic-local/"
)
credentials += Credentials(
  "Artifactory Realm",
  "suriyakrishna.jfrog.io",
  systemUser,
  systemPassword
)

pomExtra := {
  <developers>{
    Seq(("suriyakrishna", "Suriya Krishna Mohan")).map { case (id, name) =>
      <developer>
        <id>{id}</id>
        <name>{name}</name>
        <url>https://github.com/{id}</url>
      </developer>
    }
  }</developers>
}

/*
 * My Custom Publish Tasks
 */

lazy val customPublishLocal = taskKey[Unit](
  "My Customized Publish Local Command with clean, scalafmt Checks, and Jacoco Report"
)

lazy val customPublish = taskKey[Unit](
  "My Customized Publish Command with clean, scalafmt Checks, and Jacoco Report"
)

lazy val myPublish: TaskKey[Unit] => Def.Initialize[Task[Unit]] = {
  publishTask =>
    {
      (publishTask
        dependsOn (Test / jacoco)
        dependsOn (IntegrationTest / scalafmtSbtCheck)
        dependsOn (IntegrationTest / scalafmtCheckAll)
        dependsOn clean)
    }
}

customPublishLocal := myPublish(publishLocal).value
customPublish := myPublish(publish).value

/*
 * Git Metadata for Build
 */

lazy val writeGitMetadata = taskKey[File]("Writes Git Metadata for Build")

writeGitMetadata := {
  val filePath = "build.git.metadata"
  val buildTime = java.time.LocalDateTime.now()
  val gitBranch = Process("git rev-parse --abbrev-ref HEAD").!!
  val gitLog = Process("git log -n 1").!!
  val fileWriter: FileWriter = new FileWriter(filePath)
  val buildMetaFile = new File(filePath)
  val decorator: String =
    "##################################################################"
  fileWriter.write(decorator)
  fileWriter.write(s"\nBUILD USING GIT BRANCH: ${gitBranch}")
  fileWriter.write(s"BUILD DATETIME: ${buildTime}\n")
  fileWriter.write(decorator)
  fileWriter.write("\n" + gitLog)
  fileWriter.write(decorator)
  fileWriter.close()
  buildMetaFile
}
