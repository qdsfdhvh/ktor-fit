package io.github.seiko.ktorfit.kcp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * .
 *
 * @author 985892345
 * 2023/12/31 00:31
 */

class IrTest {

  @OptIn(ExperimentalCompilerApi::class)
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
    val clientSource = SourceFile.kotlin(
      "HttpClient.kt",
      """
        package io.ktor.client

        class HttpClient
      """.trimIndent(),
    )
    val result = KotlinCompilation().apply {
      sources = listOf(apiSource, implSource, clientSource)

      // pass your own instance of a compiler plugin
      compilerPluginRegistrars = listOf(KtorfitPluginRegistrar())

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK, result.messages)

    println("\n=============================================\n")

    val clientClass = result.classLoader.loadClass("io.ktor.client.HttpClient")
    val client = clientClass.getDeclaredConstructor().newInstance()

    val apiServiceClass = result.classLoader.loadClass("com.g985892345.network.ApiService")
    val createMethod = apiServiceClass.getMethod("create", clientClass)
    val apiService = createMethod.invoke(null, client)

    val getMethod = apiServiceClass.getMethod("get")
    val getResult = getMethod.invoke(apiService)

    println(getResult)
    assert(getResult == "get() success")
  }
}
