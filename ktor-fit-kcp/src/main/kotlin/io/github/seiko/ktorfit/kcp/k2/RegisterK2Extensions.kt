package io.github.seiko.ktorfit.kcp.k2

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import io.github.seiko.ktorfit.kcp.k2.check.KtorfitFirCheckers
import io.github.seiko.ktorfit.kcp.k2.resolve.CreateFirResolveExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@OptIn(ExperimentalCompilerApi::class)
internal fun CompilerPluginRegistrar.ExtensionStorage.registerK2Extensions(
  context: KtorfitBaseContext,
) {
  FirExtensionRegistrarAdapter.registerExtension(KtorfitFirExtensionRegistrar(context))
}

private class KtorfitFirExtensionRegistrar(
  private val baseContext: KtorfitBaseContext,
) : FirExtensionRegistrar() {
  override fun ExtensionRegistrarContext.configurePlugin() {
    +FirAdditionalCheckersExtension.Factory { session ->
      KtorfitFirCheckers(session, baseContext)
    }
    +FirDeclarationGenerationExtension.Factory { session ->
      CreateFirResolveExtension(session, baseContext)
    }
  }
}
