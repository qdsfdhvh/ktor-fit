import com.vanniktech.maven.publish.SonatypeHost

plugins {
  `java-gradle-plugin`
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
  alias(libs.plugins.buildconfig)
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  compileOnly(kotlin("gradle-plugin-api"))
}

mavenPublishing {
  publishToMavenCentral(SonatypeHost.S01, automaticRelease = false)
  signAllPublications()
  @Suppress("UnstableApiUsage")
  pom {
    description.set("use ktor like retrofit.")
  }
}

gradlePlugin {
  plugins {
    create("ktorfitGradlePlugin") {
      id = "io.github.qdsfdhvh.ktor-fit-plugin"
      implementationClass = "io.github.seiko.ktorfit.plugin.KtorfitGradlePlugin"
    }
  }
}

buildConfig {
  packageName("io.github.seiko.ktorfit.plugin")
  buildConfigField("String", "GROUP", "\"${rootProject.properties["GROUP"] as String}\"")
  buildConfigField("String", "VERSION", "\"${rootProject.properties["VERSION_NAME"] as String}\"")
}
