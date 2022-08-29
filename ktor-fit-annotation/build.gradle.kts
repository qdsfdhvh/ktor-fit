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
    ios()
    iosSimulatorArm64()
    tvos()
    macosX64()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()
    watchosX64()
    watchosArm64()
    js {
        browser()
        nodejs()
    }
}

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinMultiplatform(JavadocJar.Dokka("dokkaGfm")))
}
