package io.github.seiko.ktorfit.kcp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * .
 *
 * @author 985892345
 * 2023/12/31 00:31
 */

class IrTest {

  @Test
  fun testProxyApiClass() {
    // https://github.com/tschuchortdev/kotlin-compile-testing
    val apiSource = SourceFile.kotlin(
      "ApiService.kt",
      """
        package com.g985892345.network

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.ktor.client.HttpClient
        import kotlin.jvm.JvmStatic

        @GenerateApi
        interface ApiService {
          fun get(): String = "get() failure"
          companion object {
            @JvmStatic
            fun create(client: HttpClient) : ApiService {
              error("kcp failure")
            }
          }
        }
      """.trimIndent(),
    )
    val implSource = SourceFile.kotlin(
      "_ApiServiceImpl.kt",
      """
        package com.g985892345.network

        import io.ktor.client.HttpClient

        class _ApiServiceImpl(private val client: HttpClient) : ApiService {
          override fun get(): String = "get() success"
        }
      """.trimIndent(),
    )
    val result = kotlinCompilation(apiSource, implSource, clientSource)
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)

    val clientClass = result.classLoader.loadClass("io.ktor.client.HttpClient")
    val client = clientClass.getDeclaredConstructor().newInstance()

    val apiServiceClass = result.classLoader.loadClass("com.g985892345.network.ApiService")
    val createMethod = apiServiceClass.getMethod("create", clientClass)
    val apiService = createMethod.invoke(null, client)

    val getMethod = apiServiceClass.getMethod("get")
    val getResult = getMethod.invoke(apiService)

    assertEquals(getResult, "get() success")
  }

  @Test
  fun testErrorEnumClass() {
    val apiSource = SourceFile.kotlin(
      "ApiService.kt",
      """
        package com.g985892345.network

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi

        @GenerateApi
        enum class ApiService
      """.trimIndent(),
    )
    val result = kotlinCompilation(apiSource)
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
  }

  private fun kotlinCompilation(vararg files: SourceFile) = KotlinCompilation().apply {
    sources = files.toList()

    // pass your own instance of a compiler plugin
    compilerPluginRegistrars = listOf(KtorfitPluginRegistrar())

    inheritClassPath = true
    messageOutputStream = System.out // see diagnostics in real time
  }.compile()

  private val clientSource get() = SourceFile.kotlin(
    "HttpClient.kt",
    """
        package io.ktor.client

        class HttpClient
    """.trimIndent(),
  )
}
