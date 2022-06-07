# ktor-fit
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/ktor-fit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/ktor-fit)

use ktor like retrofit.

## Setup

Add the dependency in your common module's commonMain sourceSet

```kotlin
plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp").version("$ksp_version")
    // ...
}

kotlin {
    // ...
    sourceSets {
        dependencies {
            api("io.github.qdsfdhvh:ktor-fit-annotation:$ktorfit_version")
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    add("kspJvm", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspJvmTest", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspLinuxX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspMacosX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspWatchosX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspIosX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspJs","io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
}
```

## How to Use

Create a kotlin expect class

```kotlin
// @Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class TestApi(client: HttpClient, baseUrl: String) {
    @GET("get/{id}")
    suspend fun getData(@Path("id") id: String, @Query("name") name: String): String
}
```

And then create api

```kotlin
val client = HttpClient()
val api = TestApi(client, "https://example.api/")
```

## Thx

[Ktorfit](https://github.com/Foso/Ktorfit)
