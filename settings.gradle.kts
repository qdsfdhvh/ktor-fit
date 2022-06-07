// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version("1.6.21")
        id("com.android.application").version("7.2.0")
        id("com.android.library").version("7.2.0")
    }
}

rootProject.name = "ktor-fit"

include(
    ":ktor-fit-annotation",
    ":ktor-fit-ksp",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
