import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.ktorFitAnnotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet.ksp)
}

mavenPublishing {
    @Suppress("UnstableApiUsage")
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaGfm"),
            sourcesJar = true,
        )
    )
}
