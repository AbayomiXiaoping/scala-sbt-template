# scala-sbt-template

This repository provides files, directories, and build configuration for a Scala SBT project that can be completely inferred
to create a new Scala SBT project with minimal changes.

[![Scala CI](https://github.com/suriyakrishna/scala-sbt-template/actions/workflows/scala.yml/badge.svg)](https://github.com/suriyakrishna/scala-sbt-template/actions/workflows/scala.yml)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Issues](https://img.shields.io/github/issues/suriyakrishna/scala-sbt-template)]()  
[![Pull Requests](https://img.shields.io/github/issues-pr/suriyakrishna/scala-sbt-template)]()
[![GitHub Releases](https://img.shields.io/github/v/release/suriyakrishna/scala-sbt-template?display_name=release)]()
---

### Project Template Facilitates

- [x] Scala Sbt Project Template.
- [x] Sbt Assembly Plugin Configuration.
- [x] Sbt Universal Plugin Configuration.
- [x] Sbt Publish Configuration.
- [x] Sbt Scala Format Plugin Configuration.
- [x] Sbt Jacoco Plugin Configuration.
- [x] Sbt Custom Publish Configuration.
- [x] Sbt Git Metadata Generate and Publish Configuration.
- [x] Sbt GitHub Actions (Scala CI).

[//]: # (- Todo: Sbt Release Plugin and Sbt SonarQube Plugin)

#### Todo:-

- [ ] Sbt SonarQube Plugin Configuration.
- [ ] Sbt Release Plugin Configuration.

---

#### Scala Sbt Project Template

***Project Tree***

```text
.
├── .github
│   └── workflows
|       └── scala.yml
├── bin
│   └── my.app.wrapper.sh
├── conf
│   └── my.app.env.sh
├── project
│   ├── build.properties
|   └── plugins.sbt
├── src
|   ├── main
|   │   ├── resources
|   |   |   └── application.conf
|   │   └── scala
|   │       └── com
|   │           └── github
|   │               └── suriyakrishna
|   │                   └── sbt
|   │                       └── template
|   |                           └── MyApplication.scala
|   └── test
|       ├── resources
|       |   └── application-test.conf
|       └── scala
|           └── com
|               └── github
|                   └── suriyakrishna
|                       └── sbt
|                           └── template
|                               └── test
|                                   └── MyApplicationTest.scala
├── .gitignore
├── .scalafmt.conf
├── LICENSE
├── README.md
└── build.sbt
```

- `.github/workflows` directory holds files related to GitHub workflows.
- `bin` directory holds files related to wrapper scripts to execute the application. Subdirectories can be created if
  needed.
- `conf` holds files related to external configurations for the application and scripts. Subdirectories can be created
  if needed.

---

#### Sbt Assembly Plugin Configuration

- `"com.eed3si9n" % "sbt-assembly" % "0.15.0"` is the version used in this project.
- No additional configuration added in `build.sbt`.
- Navigate to Sbt Assembly Plugin documentation from [here](https://index.scala-lang.org/sbt/sbt-assembly/sbt-assembly).

---

#### Sbt Universal Plugin Configuration

- `"com.typesafe.sbt" % "sbt-native-packager" % "1.7.6"` is the version used in this project.
- Additional configuration to package zip file with `bin`, `conf`, and `jacoco-report` directories has been added in
  this project.
- Navigate to Sbt Universal Plugin documentation
  from [here](https://www.scala-sbt.org/sbt-native-packager/gettingstarted.html#packaging-formats).

---

#### Sbt Publish Configuration

- Additional artifacts such as universal zip package, assembly jar, and git metadata for the build has been configured.
- Publish Maven Style enabled.
- Repository Endpoint and credentials for the artifactory has been configured in the `build.sbt` file and during
  runtime, credentials will be fetched from the system environment variables `ARTIFACTORY_SYS_USER`
  and `ARTIFACTORY_SYS_PASSWORD`. This can be customized if required.
- `publishConfiguration.value.withOverwrite(true)` has been set to true and this can be dynamically handled
  using `ARTIFACTORY_OVERWRITE` system environment variable.
- `pomExtra` has been configured to add developer details to the published `pom.xml`.

---

#### Sbt Scala Format Plugin Configuration

- `"org.scalameta" % "sbt-scalafmt" % "2.4.6"` is the version used in this project.
- Additional configuration for formatting can be added to `.scalafmt.conf` file.
- Navigate to Sbt Scala Format Plugin documentation from [here](https://scalameta.org/scalafmt/).
- Sbt Scala Format [FAQs](https://scalameta.org/scalafmt/docs/faq.html#how-can-i-work-with-older-versions-of-intellij).

---

#### Sbt Jacoco Plugin Configuration

- `"com.github.sbt" % "sbt-jacoco" % "3.1.0"` is the version used in this project.
- As per documentation, coverage threshold has been set in `build.sbt`.
- Navigate to Sbt Jacoco Plugin documentation from [here](https://www.scala-sbt.org/sbt-jacoco/getting-started.html).

[//]: # (Todo:- Need to check how to integrate with SonarQube.)

**Note:**

- By default, coverage threshold are set to 0, and this has been overrided in the `build.sbt`.

---

#### Sbt Custom Publish Configuration

- Two custom publish sbt tasks has been defined in `build.sbt`.
- By executing these publish tasks we can perform publish with all the dependent tasks such `clean`, `scalafmtCheckAll`,
  `scalafmtSbtCheck`, and `jacoco` with single sbt command.

**Sbt `customPublishLocal`**
> **Task Execution:**
> ```bash
> sbt customPublishLocal
> ```
>
> **Task Steps:**
>
> `clean` 🠊 `scalafmtCheckAll` 🠊 `scalafmtSbtCheck` 🠊 `jacoco` 🠊 `publishLocal`

**Sbt `customPublish`**
> **Task Execution:**
> ```bash
> sbt customPublish
> ```
>
> **Task Steps:**
>
> `clean` 🠊 `scalafmtCheckAll` 🠊 `scalafmtSbtCheck` 🠊 `jacoco` 🠊 `publish`

---

#### Sbt Git Metadata Generate and Publish Configuration

- In order to audit build and release a custom sbt task `writeGitMetadata` has been configured in `build.sbt`. This can
  be customized if required.
- `writeGitMetadata` create a new file `build.git.metadata` in the root of the project. Later, this will be added to
  artifact during publish.

**Content of `build.git.metadata`:**

```text
##################################################################
BUILD USING GIT BRANCH: sbt-publish-config
BUILD DATETIME: 2022-01-19T09:08:58.402
##################################################################
commit 00386ac15316305f585e5f8f37c22cb8de12d45a
Author: Kishan <suriya.kishan@live.com>
Date:   Wed Jan 19 07:01:23 2022 +0530

    Added Resources Folder Main and Test
##################################################################
```

---

#### Sbt GitHub Actions (Scala CI)

Scala CI GitHub action has been configured for the Continuous Integration of this project.

> **Work Flow Steps:**
>
> `Git Check Out` 🠊 `JDK Setup` 🠊 `SBT Validate .sbt Formatting` 🠊 `SBT Validate .scala Formatting` 🠊 `SBT Jacoco Coverage` 🠊 `SBT Publish`

---
