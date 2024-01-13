package io.github.seiko.ktorfit

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCompilerApi::class)
class SimpleTest {

  @Test
  fun `expect_class_simple_test01`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt", """

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.github.seiko.ktorfit.annotation.http.GET
        import io.github.seiko.ktorfit.annotation.http.Path
        import io.github.seiko.ktorfit.annotation.http.Query
        import io.ktor.client.HttpClient

        @GenerateApi
        expect class TestService(client: HttpClient) {
          @GET("path/{id}")
          suspend fun get(
              @Path("id") id: String,
              @Query("name") name: String,
          ): String
        }
      """.trimIndent()
    )
    val result = KotlinCompilation().apply {
      sources = listOf(manualSource)
      symbolProcessorProviders = listOf(KtorfitProcessorProvider())
      inheritClassPath = true
      kspIncremental = true
    }.compile()
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  }


  @Test
  fun `interface_simple_test01`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt", """

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.github.seiko.ktorfit.annotation.http.GET
        import io.github.seiko.ktorfit.annotation.http.Path
        import io.github.seiko.ktorfit.annotation.http.Query
        import io.ktor.client.HttpClient

        @GenerateApi
        interface TestService {
          @GET("path/{id}")
          suspend fun get(
              @Path("id") id: String,
              @Query("name") name: String,
          ): String
        }
      """.trimIndent()
    )
    val result = KotlinCompilation().apply {
      sources = listOf(manualSource)
      symbolProcessorProviders = listOf(KtorfitProcessorProvider())
      inheritClassPath = true
      kspIncremental = true
    }.compile()
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  }
}
