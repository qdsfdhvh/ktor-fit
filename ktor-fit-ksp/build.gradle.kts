plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish.base")
}

dependencies {
    implementation(projects.ktorFitAnnotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet.ksp)
}
