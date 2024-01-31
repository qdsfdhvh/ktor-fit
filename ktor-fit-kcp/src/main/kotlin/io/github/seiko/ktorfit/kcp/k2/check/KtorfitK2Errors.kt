package io.github.seiko.ktorfit.kcp.k2.check

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtElement

internal object KtorfitK2ErrorMessages : BaseDiagnosticRendererFactory() {
  override val MAP = KtDiagnosticFactoryToRendererMap("KtorfitErrors").also { map ->
    map.put(
      KtorfitK2Errors.GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS,
      "@GenerateApi must be use in interface or expect class",
    )
  }
}

internal object KtorfitK2Errors {
  val GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS by error0<KtElement>()

  init {
    RootDiagnosticRendererFactory.registerFactory(KtorfitK2ErrorMessages)
  }
}
