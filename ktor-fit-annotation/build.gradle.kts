plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    macosX64()
    watchosArm64()
    watchosX64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    ios()
    linuxX64()
    sourceSets {
        val commonMain by getting
        val jvmMain by getting
        val linuxX64Main by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting
    }
}
