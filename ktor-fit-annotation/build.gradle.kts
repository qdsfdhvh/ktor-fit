import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    jvm()
    linuxX64()
    macosX64()
    watchosArm64()
    watchosX64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    ios()
    js {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting
        val jvmMain by getting
        val linuxX64Main by getting
        val macosX64Main by getting
        val watchosArm64Main by getting
        val watchosX64Main by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting
        val jsMain by getting
    }
}

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinMultiplatform(JavadocJar.Dokka("dokkaGfm")))
}
