plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  sourceSets {
    all {
      languageSettings {
        optIn("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
      }
    }
  }
  jvmToolchain(17)
}

dependencies {
  compileOnly(libs.kotlin.compiler.embeddable)

  testImplementation(kotlin("test"))
  testImplementation(libs.kotlin.compile.testing)
  testImplementation(projects.ktorFitAnnotation)
}
