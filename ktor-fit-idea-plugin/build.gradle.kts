import java.util.Properties

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.intelliJPlugin)
}

// 这里覆盖 settings.gradle.kts 中的 dependencyResolutionManagement
// 不然会 build 失败，不清楚原因
repositories {
  mavenLocal()
  mavenCentral()
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation(projects.ktorFitKcp)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version = "2023.2.5"
  plugins = listOf("Kotlin")
  updateSinceUntilBuild = false
}

tasks {
  val pluginName = "ktor-fit-extensions"
  val pluginVersion = "1.0.0"

  patchPluginXml {
    version.set(pluginVersion)
    sinceBuild.set("222")
    untilBuild.set("233.*")
  }

  buildPlugin {
    archiveFileName = "${pluginName}-${project.version}.zip"
  }

  val file = project.file("signing.properties")
  if (file.exists()) {
    val signingProp = Properties()
    signingProp.load(file.inputStream())

    signPlugin {
      certificateChainFile.set(file(signingProp.getProperty("CERTIFICATE_CHAIN")))
      privateKeyFile.set(file(signingProp.getProperty("PRIVATE_KEY")))
      password.set(signingProp.getProperty("PRIVATE_KEY_PASSWORD"))
      inputArchiveFile.set(layout.buildDirectory.file("distributions/${pluginName}-${project.version}.zip").get().asFile)
      outputArchiveFile.set(layout.buildDirectory.file("distributions/${pluginName}-${project.version}-signed.zip").get().asFile)
    }

    publishPlugin {
      distributionFile.set(layout.buildDirectory.file("distributions/${pluginName}-${project.version}-signed.zip").get().asFile)
      token.set(signingProp.getProperty("INTELLIJ_TOKEN"))
    }
  }
}
