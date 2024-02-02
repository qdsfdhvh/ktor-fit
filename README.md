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

### 1.Only Ksp

You can create a kotlin expect class, ksp will generate the actual class.

```kotlin
@Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class TestService(client: HttpClient) : TestOtherApi1, TestOtherApi2 {
    @GET("get/{id}")
    suspend fun getData(@Path("id") id: String, @Query("name") name: String): String
}

interface TestOtherApi1 {
    @GET("get1/{id}")
    suspend fun getOtherData1(): String
}

interface TestOtherApi2 {
    @GET("get2/{id}")
    suspend fun getOtherData2(): String
}
```

And then create api:

```kotlin
val client = HttpClient {
    defaultRequest {
        url("https://example.api/")
    }
}
val api = TestService(client)
```

### 2. Ksp and Kcp

This way is still experimental:

```kotlin
plugins {
    id("io.github.qdsfdhvh.ktor-fit-plugin") version $ktorfit_version
}
```

```kotlin
@GenerateApi
interface TestService {
    //...
}
```

ksp will auto generate code like:

```kotlin
class _TestServiceImpl(private val client: HttpClient) : TestService {
    //...
}
```

and kcp will auto generate code like:

```kotlin
interface TestService {
    //...
    companion object {
        fun create(client: HttpClient): TestService {
            return _TestServiceImpl(client)
        }
    }
}
```

so, you can use like this:

```kotlin
val client = HttpClient {
  defaultRequest {
    url("https://example.api/")
  }
}
val api = TestService.create(client)
```

however, at the moment you need the [ktor-fit-extensions](https://plugins.jetbrains.com/plugin/23688-ktor-fit-extensions) plugin to get the IDE to prompt for the `create(client)` function.

<img width="348" alt="image" src="https://github.com/qdsfdhvh/ktor-fit/assets/17807925/5b64ce86-d145-40ef-b727-0f01a422f50c">

## Thx

[Ktorfit](https://github.com/Foso/Ktorfit)
