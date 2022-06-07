package io.github.seiko.ktorfit.annotation.http

/**
 *  @Streaming
 *  @GET("posts")
 *  suspend fun getPostsStreaming(): HttpStatement
 *
 *  The return type has to be HttpStatement
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Streaming
