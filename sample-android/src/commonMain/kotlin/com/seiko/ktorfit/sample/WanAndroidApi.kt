package com.seiko.ktorfit.sample

import io.github.seiko.ktorfit.annotation.generator.GenerateApi
import io.github.seiko.ktorfit.annotation.http.GET
import io.ktor.client.HttpClient

interface WanAndroidApi {
  @GET("article/list/1/json")
  suspend fun articleList(): String
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class WanAndroidService1(client: HttpClient) : WanAndroidApi

@GenerateApi
interface WanAndroidService2 : WanAndroidApi {
  companion object {
    fun create(client: HttpClient): WanAndroidService2 {
      error("kcp failure")
    }
  }
}
