package io.github.seiko.ktorfit.sample

import io.github.seiko.ktorfit.annotation.generator.GenerateApi
import io.github.seiko.ktorfit.annotation.http.Field
import io.github.seiko.ktorfit.annotation.http.FieldMap
import io.github.seiko.ktorfit.annotation.http.FormUrlEncoded
import io.github.seiko.ktorfit.annotation.http.GET
import io.github.seiko.ktorfit.annotation.http.POST
import io.github.seiko.ktorfit.annotation.http.Path
import io.github.seiko.ktorfit.annotation.http.Query
import io.github.seiko.ktorfit.annotation.http.Streaming
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpStatement

interface TestApi {
    @GET("path/{id}")
    suspend fun testGet(
        @Path("id") id: String,
        @Query("name") name: String,
    ): String

    @FormUrlEncoded
    @POST("form/info")
    suspend fun testForm(
        @Field("value1") value1: Int,
        @Field("value2", encoded = true) value2: String,
        @FieldMap valueMap: Map<String, String>,
    ): String

    @Streaming
    @GET("stream")
    suspend fun testStream(): HttpStatement
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class TestApiWithBaseUrl(client: HttpClient, baseUrl: String) : TestApi {
    @GET("path11/{id}")
    suspend fun test11(
        @Path("id") id: String,
        @Query("name") name: String,
    ): String
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
@GenerateApi
expect class TestApiNoBaseUrl(client: HttpClient) : TestApi {
    @GET("path11/{id}")
    suspend fun test11(
        @Path("id") id: String,
        @Query("name") name: String,
    ): String
}
