package io.github.seiko.ktorfit

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspIncrementalLog
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class SimpleTest(private val useKSP2: Boolean) {

  companion object {
    @Parameterized.Parameters(name = "useKSP2={0}")
    @JvmStatic
    fun data() = arrayOf(
      arrayOf(true),
      arrayOf(false),
    )
  }

  @Test
  fun `expect_class_simple_test01`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt",
      """

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.github.seiko.ktorfit.annotation.http.GET
        import io.github.seiko.ktorfit.annotation.http.Path
        import io.github.seiko.ktorfit.annotation.http.Query
        import io.ktor.client.HttpClient

        @Suppress("NO_ACTUAL_FOR_EXPECT")
        @GenerateApi
        expect class TestService(client: HttpClient) {
          @GET("path/{id}")
          suspend fun get(
              @Path("id") id: String,
              @Query("name") name: String,
          ): String
        }
      """.trimIndent(),
    )
    val result = KotlinCompilation().apply {
      sources = listOf(manualSource)
      symbolProcessorProviders = mutableListOf(KtorfitProcessorProvider())
      inheritClassPath = true
      kspIncremental = true
      kspIncrementalLog = true
      multiplatform = true
    }.compile()
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  }

  @Test
  fun `interface_simple_test01`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt",
      """

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
      """.trimIndent(),
    )
    val compilation = KotlinCompilation().apply {
      if (useKSP2) {
        useKsp2()
      } else {
        languageVersion = "1.9"
      }
      sources = listOf(manualSource)
      symbolProcessorProviders = mutableListOf(KtorfitProcessorProvider())
      inheritClassPath = true
      kspIncremental = true
      kspIncrementalLog = true
    }

    val result = compilation.compile()
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  }
}
