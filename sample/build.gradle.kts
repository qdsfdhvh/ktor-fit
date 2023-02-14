@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    alias(libs.plugins.ksp)
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
    js(BOTH) {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ktorFitAnnotation)
                implementation(libs.bundles.ktor)
            }
        }
        val jvmMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
        }
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

dependencies {
    add("kspCommonMainMetadata", projects.ktorFitKsp)
    add("kspJvm", projects.ktorFitKsp)
    add("kspJvmTest", projects.ktorFitKsp)
    add("kspLinuxX64", projects.ktorFitKsp)
    add("kspMacosX64", projects.ktorFitKsp)
    add("kspWatchosX64", projects.ktorFitKsp)
    add("kspIosX64", projects.ktorFitKsp)
    add("kspJs",projects.ktorFitKsp)
}
