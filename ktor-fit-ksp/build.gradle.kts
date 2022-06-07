plugins {
    kotlin("jvm")
}

dependencies {
    implementation(projects.ktorFitAnnotation)
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.21-1.0.5")
    implementation("com.squareup:kotlinpoet-ksp:1.11.0")
}
