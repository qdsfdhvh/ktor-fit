package io.github.seiko.ktorfit.kcp.k2

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal class KtorfitFirExtensionRegistrar(
  private val baseContext: KtorfitBaseContext,
) : FirExtensionRegistrar() {
  override fun ExtensionRegistrarContext.configurePlugin() {
    +FirDeclarationGenerationExtension.Factory {
      CreateFirResolveExtension(it, baseContext)
    }
  }
}
