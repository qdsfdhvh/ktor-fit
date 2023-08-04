plugins {
    kotlin("multiplatform")
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
    js(BOTH) {
        browser()
        nodejs()
    }
}
