plugins {
  kotlin("jvm")
  alias(libs.plugins.buildconfig)
  `java-gradle-plugin`
  id("com.vanniktech.maven.publish")
}

buildConfig {
  packageName("io.github.seiko.ktorfit.plugin")
  buildConfigField("String", "VERSION", "\"${version}\"")
  buildConfigField("String", "GROUP", "\"${group}\"")
}

dependencies {
  compileOnly(kotlin("gradle-plugin-api"))
}

