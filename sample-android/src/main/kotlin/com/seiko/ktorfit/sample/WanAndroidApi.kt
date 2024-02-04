package com.seiko.ktorfit.sample

import io.github.seiko.ktorfit.annotation.generator.GenerateApi
import io.github.seiko.ktorfit.annotation.http.GET

interface WanAndroidApi {
  @GET("article/list/1/json")
  suspend fun articleList(): String
}

@GenerateApi
interface WanAndroidService : WanAndroidApi {
  // companion object {
  //   fun create(client: HttpClient): WanAndroidService {
  //     error("kcp failure")
  //   }
  // }
}
