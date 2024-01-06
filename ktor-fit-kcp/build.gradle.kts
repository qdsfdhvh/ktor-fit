plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish")
}

dependencies {
  compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

  testImplementation(kotlin("test"))
  // https://github.com/tschuchortdev/kotlin-compile-testing   no support for Kotlin 1.9
  // https://github.com/ZacSweers/kotlin-compile-testing
  testImplementation("dev.zacsweers.kctfork:core:0.4.0")
  testImplementation(projects.ktorFitAnnotation)
}