import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish.base")
}

dependencies {
    implementation(projects.ktorFitAnnotation)
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.8")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
}

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinJvm())
}
