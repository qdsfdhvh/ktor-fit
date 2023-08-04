plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    watchosX64()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()
    js(IR) {
        browser()
        nodejs()
    }
    @Suppress("OPT_IN_USAGE")
    wasm {
        browser()
        nodejs()
    }
}
