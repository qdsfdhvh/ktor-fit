package com.seiko.ktorfit.sample

import io.github.seiko.ktorfit.annotation.generator.GenerateApi
import io.github.seiko.ktorfit.annotation.http.GET
import io.ktor.client.HttpClient


@Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class WanAndroidApi(client: HttpClient) {
    @GET("article/list/1/json")
    suspend fun articleList(): String
}
