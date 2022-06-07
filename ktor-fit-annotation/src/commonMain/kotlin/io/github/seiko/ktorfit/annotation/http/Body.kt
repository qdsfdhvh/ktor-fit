package io.github.seiko.ktorfit.annotation.http

/**
 *
 *
Use this to upload data in an HTTP Body
@POST("createIssue")
fun upload(@Body issue: Issue)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Body
