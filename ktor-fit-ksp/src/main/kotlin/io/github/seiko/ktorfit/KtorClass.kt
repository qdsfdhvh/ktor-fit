package io.github.seiko.ktorfit

import com.squareup.kotlinpoet.ClassName

internal val HttpClient = ClassName("io.ktor.client", "HttpClient")

internal val HttpMethod = ClassName("io.ktor.http", "HttpMethod")
internal val Parameters = ClassName("io.ktor.http", "Parameters")
internal val takeFrom = ClassName("io.ktor.http", "takeFrom")
internal val parseQueryString = ClassName("io.ktor.http", "parseQueryString")
internal val contentType = ClassName("io.ktor.http", "contentType")

internal val body = ClassName("io.ktor.client.call", "body")

internal val request = ClassName("io.ktor.client.request", "request")
internal val prepareRequest = ClassName("io.ktor.client.request", "prepareRequest")
internal val setBody = ClassName("io.ktor.client.request", "setBody")
internal val parameterClass = ClassName("io.ktor.client.request", "parameter")
internal val urlClass = ClassName("io.ktor.client.request", "url")
internal val headerClass = ClassName("io.ktor.client.request", "header")
internal val formData = ClassName("io.ktor.client.request.forms", "formData")
internal val FormDataContent = ClassName("io.ktor.client.request.forms", "FormDataContent")
internal val MultiPartFormDataContent = ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
