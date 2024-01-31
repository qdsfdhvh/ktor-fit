plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.ksp)
  id("io.github.qdsfdhvh.ktorfit")
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    all {
      languageSettings {
        languageVersion = "2.0"
      }
    }
    commonMain {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(projects.ktorFitAnnotation)
        implementation(libs.bundles.ktor)
        implementation(libs.ktor.client.okhttp)
      }
    }
    androidMain {
      dependencies {
        implementation("androidx.activity:activity-compose:1.9.0-alpha01")
      }
    }
  }
  targets.all {
    compilations.all {
      compilerOptions.configure {
        freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }
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
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}

dependencies {
  add("kspAndroid", projects.ktorFitKsp)
  add("kspJvm", projects.ktorFitKsp)
}
