import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.maven.publish) apply false
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
}

spotless {
  kotlin {
    target("**/*.kt")
    targetExclude("**/build/")
    ktlint(libs.versions.ktlint.get())
  }
  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/")
    ktlint(libs.versions.ktlint.get())
  }
}

allprojects {
  group = rootProject.properties["GROUP"] as String
  version = rootProject.properties["VERSION_NAME"] as String

  plugins.withId("com.vanniktech.maven.publish") {
    @Suppress("UnstableApiUsage")
    configure<MavenPublishBaseExtension> {
      publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
      signAllPublications()
      pom {
        name.set(project.name)
        description.set("use ktor like retrofit.")
      }
    }
  }

  configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
      substitute(module("io.github.qdsfdhvh:ktor-fit-kcp")).using(project(":ktor-fit-kcp"))
    }
  }
}
