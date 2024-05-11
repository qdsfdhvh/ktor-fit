plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.composeCompiler)
  alias(libs.plugins.ksp)
  id("io.github.qdsfdhvh.ktor-fit-plugin")
}

kotlin {
  jvmToolchain(17)
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
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

composeCompiler {
  // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
  enableStrongSkippingMode = true
}

dependencies {
  implementation("androidx.activity:activity-compose:1.9.0-alpha01")
  implementation("androidx.compose.material3:material3:1.1.2")

  implementation(libs.bundles.ktor)
  implementation(libs.ktor.client.okhttp)
  implementation(projects.ktorFitAnnotation)
  ksp(projects.ktorFitKsp)
}
