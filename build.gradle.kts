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
        description.set("use ktor like retrofit.")
        name.set(project.name)
        url.set("https://github.com/qdsfdhvh/ktor-fit")
        licenses {
          license {
            name.set("MIT")
            url.set("https://opensource.org/licenses/MIT")
            distribution.set("repo")
          }
        }
        developers {
          developer {
            id.set("Seiko")
            name.set("SeikoDes")
            email.set("seiko_des@outlook.com")
          }
        }
        scm {
          url.set("https://github.com/qdsfdhvh/ktor-fit")
          connection.set("scm:git:git://github.com/qdsfdhvh/ktor-fit.git")
          developerConnection.set("scm:git:git://github.com/qdsfdhvh/ktor-fit.git")
        }
      }
    }
  }

  configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
      substitute(module("io.github.qdsfdhvh:ktor-fit-kcp")).using(project(":ktor-fit-kcp"))
    }
  }
}
