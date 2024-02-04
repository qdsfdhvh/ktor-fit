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
  val pluginVersion = "${project.version}"

  patchPluginXml {
    version.set(pluginVersion)
    sinceBuild.set("223")
    // untilBuild.set("233.*")
  }

  buildPlugin {
    archiveFileName = "$pluginName-${project.version}.zip"
  }

  signPlugin {
    inputArchiveFile.set(layout.buildDirectory.file("distributions/$pluginName-${project.version}.zip").get().asFile)
    outputArchiveFile.set(layout.buildDirectory.file("distributions/$pluginName-${project.version}-signed.zip").get().asFile)
  }

  publishPlugin {
    distributionFile.set(layout.buildDirectory.file("distributions/$pluginName-${project.version}-signed.zip").get().asFile)
  }

  val file = project.file("signing.properties")
  if (file.exists()) {
    val signingProp = Properties()
    signingProp.load(file.inputStream())
    signPlugin {
      certificateChainFile.set(file(signingProp.getProperty("CERTIFICATE_CHAIN_FILE")))
      privateKeyFile.set(file(signingProp.getProperty("PRIVATE_KEY_FILE")))
      password.set(signingProp.getProperty("PRIVATE_KEY_PASSWORD"))
    }
    publishPlugin {
      token.set(signingProp.getProperty("INTELLIJ_TOKEN"))
    }
  } else {
    signPlugin {
      certificateChain.set(System.getProperty("IDEA_CERTIFICATE_CHAIN"))
      privateKey.set(System.getProperty("IDEA_PRIVATE_KEY"))
      password.set(System.getProperty("IDEA_PRIVATE_KEY_PASSWORD"))
    }
    publishPlugin {
      token.set(System.getProperty("IDEA_INTELLIJ_TOKEN"))
    }
  }
}
