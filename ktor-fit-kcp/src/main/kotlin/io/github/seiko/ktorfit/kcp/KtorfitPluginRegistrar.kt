package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.ir.KtorfitIrGenerationExtension
import io.github.seiko.ktorfit.kcp.k1.KtorfitResolveExtension
import io.github.seiko.ktorfit.kcp.k2.KtorfitFirExtensionRegistrar
import io.github.seiko.ktorfit.kcp.utils.KtorfitLogger
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:36
 */
@OptIn(ExperimentalCompilerApi::class)
class KtorfitPluginRegistrar : CompilerPluginRegistrar() {
  override val supportsK2: Boolean
    get() = true

  override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
    val context = KtorfitBaseContext(
      logger = KtorfitLogger(configuration),
    )
    SyntheticResolveExtension.registerExtension(KtorfitResolveExtension(context))
    FirExtensionRegistrarAdapter.registerExtension(KtorfitFirExtensionRegistrar(context))
    IrGenerationExtension.registerExtension(KtorfitIrGenerationExtension(context))
  }
}
