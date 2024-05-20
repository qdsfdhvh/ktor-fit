package io.github.seiko.ktorfit.annotation

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspIncrementalLog
import com.tschuchort.compiletesting.symbolProcessorProviders
import io.github.seiko.ktorfit.KtorfitProcessorProvider
import kotlin.test.Test
import kotlin.test.assertEquals

class FormUrlEncodedTest {
  @Test
  fun `field_test`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt",
      """

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.github.seiko.ktorfit.annotation.http.POST
        import io.github.seiko.ktorfit.annotation.http.Field
        import io.github.seiko.ktorfit.annotation.http.FormUrlEncoded
        import io.ktor.client.HttpClient

        @GenerateApi
        interface TestService {
          @OptIn(ExperimentalUnsignedTypes::class)
          @POST("form11")
          @FormUrlEncoded
          suspend fun form(
            @Field(encoded = true)
            name: String,
            @Field("bool1")
            boolValue: Boolean,
            @Field("bool2")
            boolValueNullable: Boolean?,
            @Field
            booleanArray: BooleanArray?,
            @Field
            numberValue: Number,
            @Field
            uIntValue: UInt,
            @Field
            uShortValue: UShort,
            @Field
            numValue: Int,
            @Field
            longValue: Long,
            @Field
            doubleValue: Double,
            @Field
            floatValue: Float,
            @Field
            shortValue: Short,
            @Field
            byteValue: Byte,
            @Field
            charValue: Char,
            @Field
            charArrayValue: CharArray,
            @Field(encoded = true)
            byteArrayValue: CharArray,
            @Field(encoded = true)
            byteArrayValue2: CharArray?,
            @Field
            charSequenceValue: CharSequence,
            @Field
            stringArray: Array<String>,
            @Field
            stringNullableArray: Array<String?>,
            @Field
            stringList: List<String>,
            @Field
            stringNullableList: List<String?>,
            @Field
            stringCollection: Collection<String>?,
            @Field
            stringIterable: Iterable<String?>?,
            @Field
            stringSet: Set<String?>,
            @Field
            stringMutableList: MutableList<String>,
            @Field
            stringMutableSet: MutableSet<String>,
            @Field
            stringMutableCollection: MutableCollection<String>,
            @Field
            stringMutableIterable: MutableIterable<String>,
            @Field
            uIntArray: UIntArray,
            @Field
            uLongArray: ULongArray,
            @Field
            uShortArray: UShortArray,
          ): String
        }
      """.trimIndent(),
    )
    val compilation = KotlinCompilation().apply {
      sources = listOf(manualSource)
      symbolProcessorProviders = mutableListOf(KtorfitProcessorProvider())
      inheritClassPath = true
      kspIncremental = true
      kspIncrementalLog = true
    }

    val result = compilation.compile()
    assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  }

  @Test
  fun `field_map_test`() {
    val manualSource = SourceFile.kotlin(
      "TestService.kt",
      """

        import io.github.seiko.ktorfit.annotation.generator.GenerateApi
        import io.github.seiko.ktorfit.annotation.http.POST
        import io.github.seiko.ktorfit.annotation.http.FieldMap
        import io.github.seiko.ktorfit.annotation.http.FormUrlEncoded
        import io.ktor.client.HttpClient

        @GenerateApi
        interface TestService {
          @OptIn(ExperimentalUnsignedTypes::class)
          @POST("form11")
          @FormUrlEncoded
          suspend fun form(
            @FieldMap(encoded = true)
            map: Map<String, String>,
          ): String
        }
      """.trimIndent(),
    )
    val compilation = KotlinCompilation().apply {
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
