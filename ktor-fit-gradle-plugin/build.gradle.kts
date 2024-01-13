plugins {
  `java-gradle-plugin`
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
  alias(libs.plugins.buildconfig)
}

buildConfig {
  packageName("io.github.seiko.ktorfit.plugin")
  buildConfigField("String", "GROUP", "\"${rootProject.properties["GROUP"] as String}\"")
  buildConfigField("String", "VERSION", "\"${rootProject.properties["VERSION_NAME"] as String}\"")
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  compileOnly(kotlin("gradle-plugin-api"))
}
