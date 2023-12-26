plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()
    jvm()
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.ktorFitAnnotation)
                implementation(libs.bundles.ktor)
                implementation(libs.ktor.client.okhttp)
            }
        }
        androidMain {
            dependencies {
                implementation(compose.material3)
                implementation("androidx.activity:activity-compose:1.9.0-alpha01")
            }
        }
    }
    jvmToolchain(11)
}

android {
    namespace = "com.seiko.ktorfit.sample"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.seiko.ktorfit.sample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("debug.keystore")
            storePassword = "123456"
            keyAlias = "debug_alias"
            keyPassword = "123456"
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}

dependencies {
    add("kspAndroid", projects.ktorFitKsp)
    add("kspJvm", projects.ktorFitKsp)
}
