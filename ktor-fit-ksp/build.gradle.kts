plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  compileOnly(libs.ksp.api)
  implementation(projects.ktorFitAnnotation)
  implementation(libs.kotlinpoet.ksp)
  testImplementation(kotlin("test"))
  testImplementation(libs.kotlin.compile.testing.ksp)
  testImplementation(libs.ktor.client.core)
}
