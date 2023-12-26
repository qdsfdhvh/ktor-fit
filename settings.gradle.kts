// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ktor-fit"

include(
    ":ktor-fit-annotation",
    ":ktor-fit-ksp",
    ":sample",
    ":sample-android",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
