# ktor-fit
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/ktor-fit-annotation/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.qdsfdhvh/ktor-fit-annotation)

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
    // add("kspCommonMainMetadata", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    add("kspJvm", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspMacosX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspIosX64", "io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    // add("kspJs","io.github.qdsfdhvh:ktor-fit-ksp:$ktorfit_version")
    //...
}
```

## How to Use

Create a kotlin expect class

```kotlin
// @Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class TestService(client: HttpClient, /* baseUrl: String */) /* : TestOtherApi1, TestOtherApi2 */ {
    @GET("get/{id}")
    suspend fun getData(@Path("id") id: String, @Query("name") name: String): String
}

// interface TestOtherApi1 {
//     @GET("get1/{id}")
//     suspend fun getOtherData1(): String
// }
//
// interface TestOtherApi2 {
//     @GET("get2/{id}")
//     suspend fun getOtherData2(): String
// }
```

And then create api

```kotlin
val client = HttpClient {
    defaultRequest {
        url("https://example.api/")
    }
}
val api = TestService(client)
```

## Thx

[Ktorfit](https://github.com/Foso/Ktorfit)
