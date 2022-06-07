package io.github.seiko.ktorfit.annotation.http

/**
 *

&#64;GET()

suspend fun request(@Url url: String): List< Comment>

 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Url
