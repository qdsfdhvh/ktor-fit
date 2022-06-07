package io.github.seiko.ktorfit.annotation.http

/**
 *  Add headers to a request
 *
 * @ GET("comments")
 * suspend fun requestWithHeaders( @HeaderMap headerMap : Map<String,String>): List<Comment>
 *
 * @see Headers
 * @see Header
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class HeaderMap
