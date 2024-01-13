plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation(projects.ktorFitAnnotation)
  implementation(libs.ksp.api)
  implementation(libs.kotlinpoet.ksp)
}
