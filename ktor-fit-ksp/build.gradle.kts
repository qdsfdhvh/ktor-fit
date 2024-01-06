plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.ktorFitAnnotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet.ksp)
}
