plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.dokka)
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
  js {
    browser()
    nodejs()
  }
  @Suppress("OPT_IN_USAGE")
  wasmJs {
    browser()
    nodejs()
  }
  jvmToolchain(17)
}
