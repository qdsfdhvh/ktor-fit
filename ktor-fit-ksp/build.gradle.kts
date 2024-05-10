plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
}

kotlin {
  sourceSets.all {
    languageSettings {
      optIn("com.google.devtools.ksp.KspExperimental")
    }
  }
  jvmToolchain(17)
  tasks {
    test {
      // KSP2 needs more memory to run
      minHeapSize = "1024m"
      maxHeapSize = "1024m"
    }
  }
}

dependencies {
  compileOnly(libs.ksp.api)
  testCompileOnly(libs.ksp.api)
  implementation(projects.ktorFitAnnotation)
  implementation(libs.kotlinpoet.ksp)
  testImplementation(kotlin("test"))
  testImplementation(libs.kotlin.compile.testing.ksp)
  testImplementation(libs.ktor.client.core)
}
