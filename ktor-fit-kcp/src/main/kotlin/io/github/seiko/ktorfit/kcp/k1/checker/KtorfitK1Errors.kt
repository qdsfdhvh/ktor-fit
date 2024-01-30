package io.github.seiko.ktorfit.kcp.k1.checker

import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.DiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.psi.KtElement

internal object KtorfitK1ErrorMessages : DefaultErrorMessages.Extension {
  override fun getMap(): DiagnosticFactoryToRendererMap {
    return DiagnosticFactoryToRendererMap("KtorfitErrors").also { map ->
      map.put(
        KtorfitK1Errors.GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS,
        "@GenerateApi must be use in interface or expect class",
      )
    }
  }
}

internal object KtorfitK1Errors {
  @JvmField
  val GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS = DiagnosticFactory0.create<KtElement>(Severity.ERROR)
  init {
    Errors.Initializer.initializeFactoryNamesAndDefaultErrorMessages(KtorfitK1Errors::class.java, KtorfitK1ErrorMessages)
  }
}
