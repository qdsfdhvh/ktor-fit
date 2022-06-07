plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp").version("1.6.21-1.0.5")
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
        val commonMain by getting {
            dependencies {
                implementation(projects.ktorFitAnnotation)
                val ktorVersion = "2.0.2"
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
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
