package io.github.seiko.ktorfit.kcp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test

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
      "ApiService.kt", """
        package com.g985892345.network

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.ktor.client.HttpClient

        @GenerateApi
        class ApiService(val client: HttpClient) {
          fun get(): String = error("kcp failure")

          fun dumpTest() {
            test(client)
          }
          
          fun test(client: HttpClient) {
            
          }
        }
      """.trimIndent()
    )
    val implSource = SourceFile.kotlin(
      "_ApiServiceImpl.kt", """
        package com.g985892345.network

        import io.ktor.client.HttpClient

        object _ApiServiceImpl {
          fun get(client: HttpClient): String {
            return "kcp success"
          }
        }
      """.trimIndent()
    )
    val clientSource = SourceFile.kotlin(
      "HttpClient.kt",
      """
        package io.ktor.client

        class HttpClient
      """.trimIndent()
    )
    val result = KotlinCompilation().apply {
      sources = listOf(apiSource, implSource, clientSource)

      // pass your own instance of a compiler plugin
      commandLineProcessors = listOf(KtorfitProcessor())
      compilerPluginRegistrars = listOf(KtorfitRegistrar())

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    assert(result.exitCode == KotlinCompilation.ExitCode.OK) {
      result.messages
    }

    println("\n=============================================\n")

    val apiServiceClass = result.classLoader.loadClass("com.g985892345.network.ApiService")
    val clientClass = result.classLoader.loadClass("io.ktor.client.HttpClient")
    val client = clientClass.getDeclaredConstructor().newInstance()
    val apiService = apiServiceClass.getDeclaredConstructor(clientClass).newInstance(client)
    val getMethod = apiServiceClass.getMethod("get")
    val getResult = getMethod.invoke(apiService)
    println(getResult)
    assert(getResult == "kcp success")
  }
}