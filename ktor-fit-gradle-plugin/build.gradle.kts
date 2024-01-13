plugins {
  `java-gradle-plugin`
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.publish)
}

buildConfig {
  packageName("io.github.seiko.ktorfit.plugin")
  buildConfigField("String", "GROUP", "\"${rootProject.properties["GROUP"] as String}\"")
  buildConfigField("String", "VERSION", "\"${rootProject.properties["VERSION_NAME"] as String}\"")
}

dependencies {
  compileOnly(kotlin("gradle-plugin-api"))
}

