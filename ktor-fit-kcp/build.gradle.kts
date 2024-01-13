plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ksp)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  compileOnly(libs.kotlin.compiler.embeddable)

  compileOnly(libs.auto.service.annotations)
  ksp(libs.auto.service.ksp)

  testImplementation(kotlin("test"))
  testImplementation(libs.kotlin.compile.testing)
  testImplementation(projects.ktorFitAnnotation)
}
