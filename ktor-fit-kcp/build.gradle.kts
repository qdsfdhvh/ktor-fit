plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  jvmToolchain(17)
  sourceSets {
    all {
      languageSettings {
        // languageVersion = "2.0"
        optIn("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
      }
    }
  }
}

dependencies {
  compileOnly(libs.kotlin.compiler.embeddable)

  testImplementation(kotlin("test"))
  testImplementation(libs.kotlin.compile.testing)
  testImplementation(projects.ktorFitAnnotation)
}
